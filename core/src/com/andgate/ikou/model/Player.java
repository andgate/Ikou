/*
    This file is part of Ikou.
    Ikou is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License.
    Ikou is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with Ikou.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.andgate.ikou.model;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;
import com.andgate.ikou.controller.PlayerController.DirectionListener;
import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.render.PlayerRender;
import com.andgate.ikou.utility.AcceleratedTween;
import com.andgate.ikou.utility.LinearTween;
import com.andgate.ikou.utility.Vector3i;
import com.andgate.ikou.utility.graphics.ColorUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class Player implements DirectionListener, Disposable
{
    private static final String TAG = "PlayerTransformer";
    private final Ikou game;

    public Matrix4 transform = new Matrix4();

    private int depth;
    private Level level;
    private final PlayerRender playerRender;

    private AcceleratedTween fallingTween = new AcceleratedTween();

    private static final float SLIDE_SPEED = Constants.TILE_LENGTH * 15.0f / 1.0f; // units per second

    private static final float ROUGH_SLIDE_DISTANCE = Constants.TILE_LENGTH; // units
    private static final float ROUGH_SLIDE_INITIAL_SPEED = SLIDE_SPEED;
    private static final float ROUGH_SLIDE_FINAL_SPEED = 0;
    private static final float ROUGH_SLIDE_AVERAGE_SPEED = (ROUGH_SLIDE_FINAL_SPEED + ROUGH_SLIDE_INITIAL_SPEED) / 2.0f;
    private static final float ROUGH_SLIDE_TIME = ROUGH_SLIDE_DISTANCE / ROUGH_SLIDE_AVERAGE_SPEED;
    private static final float ROUGH_SLIDE_DECCELERATION = (ROUGH_SLIDE_FINAL_SPEED - ROUGH_SLIDE_INITIAL_SPEED) / ROUGH_SLIDE_TIME; // units per second per second

    private static final float FALL_SPEEDUP_FACTOR = 4.0f;
    private static final float FALL_DISTANCE = Constants.FLOOR_SPACING;
    private static final float FALL_SPEED_INITIAL = FALL_DISTANCE / 1.0f; // units per second
    private static final float FALL_SPEED_FINAL = FALL_SPEED_INITIAL * FALL_SPEEDUP_FACTOR; // units per second
    private static final float FALL_SPEED_AVERAGE = (FALL_SPEED_INITIAL + FALL_SPEED_FINAL) / 2.0f; // units per second
    private static final float FALL_TIME = FALL_DISTANCE / FALL_SPEED_AVERAGE; // seconds
    private static final float FALL_ACCELERATION = (FALL_SPEED_FINAL - FALL_SPEED_INITIAL) / FALL_TIME; // units per second per second

    private Vector3 position = new Vector3();
    private Vector3i direction = new Vector3i();

    public Player(final Ikou game, final Level level, final int depth)
    {
        this.game = game;
        this.level = level;

        playerRender = new PlayerRender();
        playerRender.setColor(level.getFloor(depth).getPalette().player);
        playerRender.getTransform().set(transform);

        this.depth = depth;
        setPosition(level.getStartPosition(depth));
    }

    public PlayerRender getRender()
    {
        return playerRender;
    }

    public void setPosition(float x, float y, float z)
    {
        position.set(x, y, z);
        initialPosition.set(x, y, z);
        finalPosition.set(x, y, z);
        transform.idt().translate(x,y,z);
        playerRender.getTransform().idt().set(transform);
    }

    public void setPosition(Vector3 position)
    {
        setPosition(position.x, position.y, position.z);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    private Vector3 distance = new Vector3();
    public void update(float delta)
    {
        Tile currentTile = getCurrentTile();

        if(!direction.isZero() || currentTile == Tile.End)
        {
            distance.set(getPosition());

            if(currentTile == Tile.End)
            {
                fallToNextFloor(delta);
            }
            else
            {
                updateNextTile(delta);
            }

            distance.sub(getPosition());
            distance.scl(-1);

            notifyPlayerTransformListeners(distance.x, distance.y, distance.z);

            playerRender.getTransform().idt().translate(position);
        }
    }

    public void updateNextTile(float delta)
    {
        Tile nextTile = getNextTile();

        switch(nextTile)
        {
            case Smooth:
                slideSmooth(delta);
                break;
            case Obstacle:
                hitObstacle();
                break;
            case Blank:
                hitObstacle();
                break;
            case Rough:
                slideRough(delta);
                break;
            case End:
                slideEnd(delta);
                break;
            default:
                break;
        }
    }

    boolean slideStarted = false;

    private LinearTween slideSmoothTween = new LinearTween();
    private boolean slideSmooth(float delta)
    {
        if(!slideStarted)
        {
            slideSmoothTween.setup(initialPosition, finalPosition, SLIDE_SPEED);
            slideStarted = true;
        }

        boolean isSlidingOver = slideSmoothTween.update(delta);
        position.set(slideSmoothTween.get());

        if(isSlidingOver)
        {
            slideStarted = false;
            moveInDirection(direction.x, direction.z, true);

            float leftOverDelta = slideSmoothTween.getLeftOverTime();

            if(leftOverDelta > 0.0f)
            {
                // Move completely if the next tile is smooth
                Tile nextTile = getNextTile();

                switch(nextTile)
                {
                    case Smooth:
                        slideSmooth(leftOverDelta);
                        break;
                    case Rough:
                        slideRough(leftOverDelta);
                        break;
                    case End:
                        slideEnd(leftOverDelta);
                        break;
                    default:
                        break;
                }
            }
        }

        return isSlidingOver;
    }


    private AcceleratedTween slideRoughTween = new AcceleratedTween();
    private boolean slideRough(float delta)
    {
        return slideStopGradually(delta, game.roughSound);
    }

    private boolean slideEnd(float delta)
    {
        return slideStopGradually(delta, null);
    }

    private boolean slideStopGradually(float delta, Sound soundEffect)
    {
        if(!slideStarted)
        {
            if(soundEffect != null)
            {
                soundEffect.play(0.2f);
            }

            slideRoughTween.setup(initialPosition, finalPosition, SLIDE_SPEED, ROUGH_SLIDE_DECCELERATION);
            slideStarted = true;
        }

        boolean isSlidingOver = slideRoughTween.update(delta);
        position.set(slideRoughTween.get());


        if(isSlidingOver)
        {
            slideStarted = false;
            direction.set(0,0,0);

            initialPosition.set(position);
            saveProgress();
        }

        return isSlidingOver;
    }

    private void hitObstacle()
    {
        game.hitSound.play(0.5f);

        direction.set(0, 0, 0);
        initialPosition.set(position);
        saveProgress();
    }

    private boolean isFalling = false;
    private boolean fallToNextFloor(float delta)
    {
        if(!isFalling)
        {
            game.fallSound.play(0.5f);

            initialPosition.set(position);
            finalPosition.set(position);
            finalPosition.y -= Constants.FLOOR_SPACING;
            fallingTween.setup(initialPosition, finalPosition, FALL_SPEED_INITIAL, FALL_ACCELERATION);

            isFalling = true;
        }

        boolean isFallingOver = fallingTween.update(delta);
        position.set(fallingTween.get());

        tweenBackground(fallingTween.getPercentComplete());
        tweenPlayerColor(fallingTween.getPercentComplete());

        if(isFallingOver)
        {
            isFalling = false;
            direction.set(0,0,0);

            initialPosition.set(position);
            game.hitSound.play();

            startNextFloor();
        }

        return isFallingOver;
    }

    private Color tmpBg = new Color();
    public void tweenBackground(float percent)
    {
        Color lastFloorColor = level.getFloor(depth).getPalette().background;
        Color nextFloorColor = level.getFloor(depth + 1).getPalette().background;

        ColorUtils.tween(lastFloorColor, nextFloorColor, percent, tmpBg);
        game.bloom.setClearColor(tmpBg.r, tmpBg.g, tmpBg.b, tmpBg.a);
    }

    private Color tmpColor = new Color();
    public void tweenPlayerColor(float percent)
    {
        Color lastFloorColor = level.getFloor(depth).getPalette().player;
        Color nextFloorColor = level.getFloor(depth + 1).getPalette().player;

        ColorUtils.tween(lastFloorColor, nextFloorColor, percent, tmpColor);
        playerRender.setColor(tmpColor);
    }

    public void startNextFloor()
    {
        depth++;
        level.startNextFloorThreaded(depth);
        saveProgress();
    }

    public void saveProgress()
    {
        Preferences prefs = Gdx.app.getPreferences(Constants.PLAYER_PREFS);

        prefs.putInteger(Constants.PLAYER_PREF_DEPTH, depth);
        prefs.putLong(Constants.PLAYER_PREF_LEVEL_SEED, level.getSeed());
        prefs.putFloat(Constants.PLAYER_PREF_X, position.x);
        prefs.putFloat(Constants.PLAYER_PREF_Y, position.y);
        prefs.putFloat(Constants.PLAYER_PREF_Z, position.z);

        prefs.flush();
    }

    Vector3 initialPosition = new Vector3();
    Vector3 finalPosition = new Vector3();

    @Override
    public void moveInDirection(Vector2 direction)
    {
        assert(direction.isUnit());

        if(this.direction.isZero())
        {
            Tile nextTile = getTile((int) (position.x + direction.x), (int) (position.z + direction.y));

            if(!(nextTile == Tile.Obstacle || nextTile == Tile.Blank))
            {
                moveInDirection((int)direction.x, (int)direction.y, false);
            }
        }
    }

    public void moveInDirection(int x, int z, boolean forceMove)
    {
        if((direction.isZero() && !isFalling) || forceMove)
        {
            direction.set(x, 0, z);

            initialPosition.set(position);

            finalPosition.set(position);
            finalPosition.add(direction.x, direction.y, direction.z);
        }
    }

    public Tile getNextTile()
    {
        return getTile(finalPosition);
    }

    public Tile getCurrentTile()
    {
        return getTile(initialPosition);
    }

    public Tile getTile(int x, int z)
    {
        TileStack tileStack = level.getTileStack(depth, x, z);
        Tile tile = tileStack.getTop();

        return tile;
    }

    public Tile getTile(Vector3 tileLocation)
    {
        int x = (int)tileLocation.x;
        int z = (int)tileLocation.z;

        return getTile(x, z);
    }

    private ArrayList<PlayerTransformListener> playerTransformListeners = new ArrayList<>();

    public interface PlayerTransformListener
    {
        public void playerTransformModified(float dx, float dy, float dz);
    }

    public void addPlayerTransformListener(PlayerTransformListener playerTransformListener)
    {
        playerTransformListeners.add(playerTransformListener);
    }

    public void removePlayerTransformListener(PlayerTransformListener playerTransformListener)
    {
        playerTransformListeners.remove(playerTransformListener);
    }

    public void notifyPlayerTransformListeners(float x, float y, float z)
    {
        for(PlayerTransformListener playerTransformListener : playerTransformListeners)
        {
            playerTransformListener.playerTransformModified(x, y, z);
        }
    }

    @Override
    public void dispose()
    {
        playerRender.dispose();
    }
}

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
import com.andgate.ikou.controller.PlayerDirectionGestureDetector.DirectionListener;
import com.andgate.ikou.io.ProgressDatabaseService;
import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.utility.AcceleratedTween;
import com.andgate.ikou.utility.LinearTween;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Player implements DirectionListener
{
    private static final String TAG = "PlayerTransformer";
    private final Ikou game;

    public Matrix4 transform = new Matrix4();

    private int currentFloor;
    private Level level;

    private AcceleratedTween fallingTween = new AcceleratedTween();

    private static final float SLIDE_SPEED = 15.0f; // units per second
    private static final float SLIDE_ROUGH_DECCELERATION = -10.0f; // units per second
    private static final float FALL_SPEED = (Constants.FLOOR_SPACING) / 1.0f; // units per second
    private static final float FALL_ACCELERATION = 100.0f; // units per second

    private Vector3 position = new Vector3();
    private Vector3i direction = new Vector3i();

    public Player(final Ikou game, final Level level, final int startingFloor)
    {
        this.game = game;
        this.level = level;

        this.currentFloor = startingFloor;
        position.set(level.getStartPosition(currentFloor - 1));
    }

    public void setPosition(float x, float y, float z)
    {
        this.position.set(x, y, z);
        transform.idt().translate(position);
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
            slideSmoothTween.setup(initialPosition, finalPosition, SLIDE_SPEED, true);

            //game.fallSound.stop();
            //game.fallSound.play();

            slideStarted = true;
        }

        boolean isSlidingOver = slideSmoothTween.update(delta);
        position.set(slideSmoothTween.get());

        if(isSlidingOver)
        {
            slideStarted = false;
            moveInDirection(direction.x, direction.z, true);

            // Move completely is the next tile is smooth
            Tile nextTile = getNextTile();
            if(nextTile == Tile.Smooth)
            {
                slideSmooth(0.0f);
            }
        }

        return isSlidingOver;
    }


    private AcceleratedTween slideRoughTween = new AcceleratedTween();
    private boolean slideRough(float delta)
    {
        if(!slideStarted)
        {
            slideRoughTween.setup(initialPosition, finalPosition, SLIDE_SPEED, SLIDE_ROUGH_DECCELERATION);

            game.fallSound.stop();
            game.fallSound.play();

            slideStarted = true;
        }

        boolean isSlidingOver = slideRoughTween.update(delta);
        position.set(slideRoughTween.get());

        if(isSlidingOver)
        {
            slideStarted = false;
            direction.set(0,0,0);

            initialPosition.set(position);
        }

        return isSlidingOver;
    }

    private void slideEnd(float delta)
    {
        slideRough(delta);
    }

    private void hitObstacle()
    {
        direction.set(0,0,0);
        game.fallSound.stop();
        game.hitSound.play();

        initialPosition.set(position);
        slideRoughTween.reset();
    }

    private boolean isFalling = false;
    private boolean fallToNextFloor(float delta)
    {
        if(!isFalling)
        {
            initialPosition.set(position);
            finalPosition.set(position);
            finalPosition.y -= Constants.FLOOR_SPACING;
            fallingTween.setup(initialPosition, finalPosition, FALL_SPEED, FALL_ACCELERATION);

            isFalling = true;
            game.fallSound.play();
        }

        boolean isFallingOver = fallingTween.update(delta);
        position.set(fallingTween.get());

        if(isFallingOver)
        {
            isFalling = false;
            direction.set(0,0,0);

            initialPosition.set(position);

            startNextFloor();
        }

        return isFallingOver;
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

    public void startNextFloor()
    {
        saveProgress();
        currentFloor++;
    }

    private void saveProgress()
    {
        ProgressDatabase progressDB = ProgressDatabaseService.read();
        int completedFloors = progressDB.getFloorsCompleted(level.getName());
        if(currentFloor > completedFloors)
        {
            progressDB.setFloorsCompleted(level.getName(), currentFloor);
            ProgressDatabaseService.write(progressDB);
        }
    }

    Vector3 initialPosition = new Vector3();
    Vector3 finalPosition = new Vector3();

    @Override
    public void moveInDirection(Vector2 direction)
    {
        assert(direction.isUnit());

        moveInDirection((int)direction.x, (int)direction.y, false);

        game.fallSound.play();
    }

    public void moveInDirection(int x, int z, boolean forceMove)
    {
        if(direction.isZero() || forceMove)
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

    public Tile getTile(Vector3 tileLocation)
    {
        int x = (int)tileLocation.x;
        int z = (int)tileLocation.z;
        TileStack tileStack = level.getTileStack(currentFloor, x, z);
        Tile tile = tileStack.getTop();

        return tile;
    }
}

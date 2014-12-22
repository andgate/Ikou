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

package com.andgate.ikou.view;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.TileMaze;
import com.andgate.ikou.render.PlayerModelRender;
import com.andgate.ikou.utility.LinearTween;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class Player implements Disposable, TileMaze.WinListener, TileMaze.PlayerMoveListener
{
    private static final String TAG = "Player";

    private final Level level;

    private static final float MOVE_SPEED = 15.0f; // units per second
    private static final float FALL_SPEED = Constants.FLOOR_SPACING / 0.4f; // units per second
    private boolean isMoving = false;
    private boolean isFalling = false;
    private LinearTween movementTween = new LinearTween();
    private LinearTween fallingTween = new LinearTween();
    PlayerModelRender playerModel = new PlayerModelRender();

    private ArrayList<PlayerPositionListener> playerPositionListeners = new ArrayList<>();

    public Player(Level level)
    {
        this.level = level;
        setPosition(level.getIntialPlayerPosition());

        for(TileMaze maze : level.getMazes())
        {
            maze.addWinListener(this);
            maze.addPlayerMoveListener(this);
        }
    }

    public void addPlayerPositionListener(PlayerPositionListener playerPositionListener)
    {
        playerPositionListeners.add(playerPositionListener);
    }

    public void removePlayerPositionListener(PlayerPositionListener playerPositionListener)
    {
        playerPositionListeners.remove(playerPositionListener);
    }

    public void notifyPlayerPositionListeners(float x, float y, float z)
    {
        for(PlayerPositionListener playerPositionListener : playerPositionListeners)
        {
            playerPositionListener.playerPositionModified(x, y, z);
        }
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(playerModel, environment);
    }

    /** Distance moved, for notifying listeners like the camera. **/
    private Vector3 distance = new Vector3();
    public void update(float delta)
    {
        if(isMoving || isFalling)
        {
            distance.set(getPosition());

            if (isMoving) {
                updateMovement(delta);
            } else if (isFalling) {
                updateFall(delta);
            }

            distance.sub(getPosition());
            distance.scl(-1);

            notifyPlayerPositionListeners(distance.x, distance.y, distance.z);
        }
    }

    private void updateMovement(float delta)
    {
        if(movementTween.update(delta))
        {
            isMoving = false;
        }

        setPosition(movementTween.get());
    }

    private boolean isFallingStarted = false;
    private void updateFall(float delta)
    {
        if(!isFallingStarted)
        {
            fallingTween.setup(getPosition(), level.getCurrentPlayerPosition(), FALL_SPEED);
            isFallingStarted = true;
        }
        else
        {

            if (fallingTween.update(delta)) {
                isFalling = false;
                isFallingStarted = false;
            }

            setPosition(fallingTween.get());
        }
    }

    public PlayerModelRender getModel()
    {
        return playerModel;
    }

    public boolean isMoving()
    {
        return isMoving;
    }
    public boolean isFalling()
    {
        return isFalling;
    }

    public void setPosition(Vector3 position)
    {
        playerModel.transform.idt();
        playerModel.transform.translate(position);
    }

    private Vector3 position = new Vector3();
    public Vector3 getPosition()
    {
        playerModel.transform.getTranslation(position);
        return position;
    }

    @Override
    public void dispose()
    {
        playerModel.dispose();
    }

    @Override
    public void mazeWon()
    {
        isFalling = true;
    }


    Vector3 end = new Vector3();
    @Override
    public void movePlayerBy(int x, int y)
    {
        isMoving = true;

        end.set(getPosition());
        end.add(x, 0, y);

        movementTween.setup(getPosition(), end, MOVE_SPEED);
    }

    public interface PlayerPositionListener
    {
        public void playerPositionModified(float dx, float dy, float dz);
    }
}

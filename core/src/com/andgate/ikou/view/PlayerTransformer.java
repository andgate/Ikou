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
import com.andgate.ikou.Ikou;
import com.andgate.ikou.utility.LinearTween;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class PlayerTransformer
{
    private static final String TAG = "PlayerTransformer";
    private final Ikou game;

    public Matrix4 transform = new Matrix4();

    private float currentFloor;

    private LinearTween movementTween = new LinearTween();
    private LinearTween fallingTween = new LinearTween();

    private static final float MOVE_SPEED = 15.0f; // units per second
    private static final float FALL_SPEED = Constants.FLOOR_SPACING / 0.4f; // units per second

    private boolean isFalling = false;
    private boolean isMoving = false;

    public PlayerTransformer(final Ikou game, final Vector3 initialPosition)
    {
        this.game = game;
        position.set(initialPosition);
    }

    public boolean isMoving()
    {
        return isMoving;
    }

    public boolean isFalling()
    {
        return isFalling;
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
        if(isMoving || isFalling)
        {
            distance.set(getPosition());

            if (isMoving)
            {
                updateMove(delta);
            }
            else if (isFalling)
            {
                updateFall(delta);
            }

            distance.sub(getPosition());
            distance.scl(-1);

            notifyPlayerTransformListeners(distance.x, distance.y, distance.z);
        }
    }

    private Vector3 position = new Vector3();
    private Vector3 destination = new Vector3();
    private Vector3 ground = new Vector3();

    private void updateMove(float delta)
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
            destination.set(position);
            destination.y -= Constants.FLOOR_SPACING;
            fallingTween.setup(getPosition(), destination, FALL_SPEED);
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

    public void gotoNextLevel()
    {
        if(!isFalling)
        {
            isFalling = true;
        }
    }

    public void moveBy(int x, int z)
    {
        if(!isMoving && !isFalling)
        {
            isMoving = true;
            destination.set(position);
            destination.add(x, 0.0f, z);

            movementTween.setup(position, destination, MOVE_SPEED);
        }
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
}

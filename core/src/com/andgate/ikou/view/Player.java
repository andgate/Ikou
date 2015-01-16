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
import com.andgate.ikou.controller.PlayerDirectionGestureDetector.DirectionListener;
import com.andgate.ikou.io.ProgressDatabaseService;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.ProgressDatabase;
import com.andgate.ikou.model.TileMazeSimulator;
import com.andgate.ikou.model.TileMazeSimulator.MazeWonListener;
import com.andgate.ikou.utility.AcceleratedTween;
import com.andgate.ikou.utility.LinearTween;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Player implements DirectionListener, MazeWonListener
{
    private static final String TAG = "PlayerTransformer";
    private final Ikou game;

    public Matrix4 transform = new Matrix4();

    private int currentFloor;
    private Level level;
    private TileMazeSimulator mazeSim;

    private LinearTween movementTween = new LinearTween();
    private AcceleratedTween fallingTween = new AcceleratedTween();

    private static final float MOVE_SPEED = 15.0f; // units per second
    private static final float FALL_SPEED = (Constants.FLOOR_SPACING) / 1.0f; // units per second
    private static final float FALL_ACCELERATION = 100.0f; // units per second

    private boolean isFalling = false;
    private boolean isMoving = false;

    public Player(final Ikou game, final Level level, final int startingFloor)
    {
        this.game = game;
        this.level = level;

        this.currentFloor = startingFloor;
        mazeSim = new TileMazeSimulator(level.getFloor(currentFloor));
        mazeSim.setMazeWonListener(this);
        position.set(level.getStartPosition(currentFloor - 1));
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
        updateTransform(delta);
    }

    private Vector3 position = new Vector3();
    private Vector3 destination = new Vector3();
    private Vector3 ground = new Vector3();

    private void updateTransform(float delta)
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

    private void updateMove(float delta)
    {
        if(movementTween.update(delta))
        {
            isMoving = false;
            game.fallSound.stop();
            if(!isFalling)
                game.hitSound.play();
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
            fallingTween.setup(getPosition(), destination, FALL_SPEED, FALL_ACCELERATION);
            isFallingStarted = true;
            //playing falling sound
            game.fallSound.play();
        }
        else
        {
            if (fallingTween.update(delta)) {
                isFalling = false;
                isFallingStarted = false;
                game.fallSound.stop();
                game.hitSound.play();
                // Since the falling is complete, begin the next floor!
                startNextFloor();
            }

            setPosition(fallingTween.get());
        }
    }

    public void moveBy(int x, int z)
    {
        // Check isFallingStarted to prevent mid-air movement,
        // because isFalling can be triggered by the maze sim
        if(!(isMoving || isFallingStarted))
        {
            isMoving = true;
            destination.set(position);
            destination.add(x, 0.0f, z);

            movementTween.setup(position, destination, MOVE_SPEED);

            game.fallSound.stop();
            game.fallSound.play();
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

    public void startNextFloor()
    {
        saveProgress();
        currentFloor++;

        mazeSim.setFloor(level.getFloor(currentFloor));
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


    @Override
    public void floorEndTrigger()
    {
        isFalling = true;
    }

    Vector3i tmpDirection = new Vector3i();
    @Override
    public void moveInDirection(Vector2 direction)
    {
        if(!(isMoving || isFalling))
        {
            tmpDirection.set(direction.x, 0.0f, direction.y);
            // Keep in mind that this can set isFalling to true
            Vector3i displacement = mazeSim.move(tmpDirection);

            moveBy(displacement.x, displacement.z);
        }
    }
}

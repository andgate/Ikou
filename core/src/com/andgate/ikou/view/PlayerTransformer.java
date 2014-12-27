package com.andgate.ikou.view;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.utility.LinearTween;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Vector;

public class PlayerTransformer
{
    public Matrix4 transform = new Matrix4();

    private float currentFloor;

    private LinearTween movementTween = new LinearTween();
    private LinearTween fallingTween = new LinearTween();

    private static final float MOVE_SPEED = 15.0f; // units per second
    private static final float FALL_SPEED = Constants.FLOOR_SPACING / 0.4f; // units per second

    private boolean isFalling = false;
    private boolean isMoving = false;

    public PlayerTransformer(final Vector3 initialPosition)
    {
        position.set(initialPosition);
    }

    public PlayerTransformer(final float x, final float y, final float z)
    {
        position.set(x, y, z);
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

package com.andgate.ikou.input;

import com.badlogic.gdx.math.Vector2;

public class PlayerInput
{
    private final CameraInputController cameraController;
    private final DirectionListener directionListener;

    Vector2 velocity = new Vector2();
    Vector2 direction = new Vector2();

    public PlayerInput(DirectionListener directionListener, CameraInputController cameraController)
    {
        this.cameraController = cameraController;
        this.directionListener = directionListener;
    }

    public void move(float velocityX, float velocityY)
    {
        velocity.set(velocityX, velocityY);
        velocityToDirection(velocity,direction);
        moveInDirection(direction.x, direction.y);
    }

    public void moveInDirection(float directionX, float directionY)
    {
        direction.set(directionX, directionY);
        assert(direction.isUnit());
        directionListener.moveInDirection(direction);
    }

    public void velocityToDirection(Vector2 velocity, Vector2 direction)
    {
        direction.set(0f,0f);
        velocity.rotate(-cameraController.getAngleX());

        float absVelocityX = Math.abs(velocity.x);
        float absVelocityY = Math.abs(velocity.y);
        if(absVelocityX > absVelocityY)
        {
            direction.x = -velocity.x / absVelocityX;
        }
        else if (absVelocityX < absVelocityY)
        {
            direction.y = -velocity.y / absVelocityY;
        }
    }

    public static interface DirectionListener
    {
        public void moveInDirection(Vector2 direction);
    }
}

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

package com.andgate.ikou.input;

import com.andgate.ikou.render.ThirdPersonCamera;
import com.badlogic.gdx.math.Vector2;

public class PlayerInput
{
    private final ThirdPersonCamera camera;
    private final DirectionListener directionListener;

    Vector2 velocity = new Vector2();
    Vector2 direction = new Vector2();

    public PlayerInput(DirectionListener directionListener, ThirdPersonCamera camera)
    {
        this.camera = camera;
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
        velocity.rotate(-camera.getAngleX());

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

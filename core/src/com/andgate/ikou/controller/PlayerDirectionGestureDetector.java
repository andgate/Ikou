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
package com.andgate.ikou.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayerDirectionGestureDetector extends GestureDetector
{
    private static final String TAG = "PlayerDirectionGestureDetector";

    private final DirectionGestureListener directionGestureListener;

    public PlayerDirectionGestureDetector(DirectionListener directionListener, CameraInputController cameraController) {
        this(new DirectionGestureListener(directionListener, cameraController));
    }

    public PlayerDirectionGestureDetector(DirectionGestureListener directionGestureListener)
    {
        super(directionGestureListener);
        this.directionGestureListener = directionGestureListener;
    }

    public void setDirectionListener(DirectionListener directionListener)
    {
        directionGestureListener.setDirectionListener(directionListener);
    }

    private static class DirectionGestureListener extends GestureAdapter
    {
        DirectionListener directionListener;
        private final CameraInputController cameraController;

        public DirectionGestureListener(DirectionListener directionListener, CameraInputController cameraController){
            this.directionListener = directionListener;
            this.cameraController = cameraController;
        }

        public void setDirectionListener(DirectionListener directionListener)
        {
            this.directionListener = directionListener;
        }


        Vector2 velocity = new Vector2();
        Vector2 direction = new Vector2();

        @Override
        public boolean fling(float x, float y, int button)
        {
            float directionX = 0.0f;
            float directionY = 0.0f;

            velocity.set(x, y);
            // negate the cameraController angleX (it's clockwise, rotate needs counter clockwise)
            velocity.rotate(-cameraController.getAngleX());

            float absVelocityX = Math.abs(velocity.x);
            float absVelocityY = Math.abs(velocity.y);
            if(absVelocityX > absVelocityY)
            {
                directionX = -1 * velocity.x / absVelocityX;
            }
            else if (absVelocityX < absVelocityY)
            {
                directionY = -1 * velocity.y / absVelocityY;
            }

            direction.set(directionX, directionY);
            directionListener.moveInDirection(direction);

            return super.fling(x, y, button);
        }
    }

    public static interface DirectionListener
    {
        public void moveInDirection(Vector2 direction);
    }
}
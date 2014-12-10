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
package com.andgate.ikou;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;

public class PlayerDirectionGestureDetector extends GestureDetector
{
    public PlayerDirectionGestureDetector(DirectionListener directionListener, PerspectiveCamera camera) {
        super(new DirectionGestureListener(directionListener, camera));
    }

    private static class DirectionGestureListener extends GestureAdapter{
        DirectionListener directionListener;
        private final PerspectiveCamera camera;

        public DirectionGestureListener(DirectionListener directionListener, PerspectiveCamera camera){
            this.directionListener = directionListener;
            this.camera = camera;
        }

        Vector3 velocity = new Vector3();

        Vector3 worldFoward = new Vector3(0.0f, 0.0f, 1.0f);
        Vector3 worldBackward = new Vector3(0.0f, 0.0f, -1.0f);
        Vector3 worldLeft = new Vector3(1.0f, 0.0f, 0.0f);
        Vector3 worldRight = new Vector3(-1.0f, 0.0f, 0.0f);

        Vector3 screenFoward = new Vector3();
        Vector3 screenBackward = new Vector3();
        Vector3 screenLeft = new Vector3();
        Vector3 screenRight = new Vector3();


        @Override
        public boolean fling(float velocityX, float velocityY, int button)
        {
            velocity.set(velocityX, velocityY, 0.0f);
            if(Math.abs(velocityX) > Math.abs(velocityY)){
                if(velocityX > 0){
                    directionListener.onRight();
                }else{
                    directionListener.onLeft();
                }
            }else{
                if(velocityY > 0){
                    directionListener.onDown();
                }else{
                    directionListener.onUp();
                }
            }

            screenFoward.set(worldFoward);
            camera.project(screenFoward);

            System.out.println("Screen coords: " + screenFoward.toString());
            System.out.println("World coords: " + worldFoward.toString());

            return super.fling(velocityX, velocityY, button);
        }

    }

}
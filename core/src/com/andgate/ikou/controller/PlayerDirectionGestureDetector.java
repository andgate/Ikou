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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.andgate.ikou.controller.PlayerController.DirectionListener;

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

    @Override
    public boolean keyDown(int keyCode)
    {
        PlayerController playerControls = directionGestureListener.getPlayerControls();

        switch(keyCode)
        {
            case Input.Keys.W:
            case Input.Keys.UP:
                playerControls.move(0.0f, -1.0f);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                playerControls.move(0.0f, 1.0f);
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                playerControls.move(-1.0f, 0.0f);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                playerControls.move(1.0f, 0.0f);
                break;
            default:
                break;
        }

        return super.keyDown(keyCode);
    }

    private static class DirectionGestureListener extends GestureAdapter
    {
        PlayerController playerControls;

        public DirectionGestureListener(PlayerController.DirectionListener directionListener, CameraInputController cameraController)
        {
            playerControls = new PlayerController(directionListener, cameraController);
        }

        public PlayerController getPlayerControls()
        {
            return playerControls;
        }

        @Override
        public boolean fling(float x, float y, int button)
        {
            playerControls.move(x, y);
            return super.fling(x, y, button);
        }
    }
}
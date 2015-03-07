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
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.andgate.ikou.input.PlayerInput.DirectionListener;

public class PlayerGestureDetector extends GestureDetector
{
    private static final String TAG = "PlayerDirectionGestureDetector";

    private static final int MAX_FINGERS = 1;

    private final DirectionGestureListener directionGestureListener;

    public PlayerGestureDetector(DirectionListener directionListener, ThirdPersonCamera camera) {
        this(new DirectionGestureListener(directionListener, camera));
    }

    public PlayerGestureDetector(DirectionGestureListener directionGestureListener)
    {
        super(directionGestureListener);
        this.directionGestureListener = directionGestureListener;
    }

    @Override
    public boolean keyDown(int keyCode)
    {
        PlayerInput playerInput = directionGestureListener.getPlayerInput();

        switch(keyCode)
        {
            case Input.Keys.W:
            case Input.Keys.UP:
                playerInput.move(0.0f, -1.0f);
                break;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                playerInput.move(0.0f, 1.0f);
                break;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                playerInput.move(-1.0f, 0.0f);
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                playerInput.move(1.0f, 0.0f);
                break;
            default:
                break;
        }

        return super.keyDown(keyCode);
    }

    private static class DirectionGestureListener extends GestureAdapter
    {
        private PlayerInput playerInput;

        public DirectionGestureListener(PlayerInput.DirectionListener directionListener, ThirdPersonCamera camera)
        {
            playerInput = new PlayerInput(directionListener, camera);
        }

        public PlayerInput getPlayerInput()
        {
            return playerInput;
        }

        @Override
        public boolean fling(float x, float y, int button)
        {
            if(Gdx.app.getType() != Application.ApplicationType.Android) return false;
            if(Gdx.input.isTouched(1)) return false;

            playerInput.move(x, y);
            return false;
        }
    }
}
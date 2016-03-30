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

package com.andgate.ikou.input

import com.andgate.ikou.actor.MazeActor
import com.andgate.ikou.input.PlayerInputAdjuster
import com.andgate.ikou.graphics.camera.ThirdPersonCamera
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureAdapter

class PlayerGestureDetector(val directionGestureListener: DirectionGestureListener)
: GestureDetector(directionGestureListener)
{
    private val TAG: String = "PlayerDirectionGestureDetector"

    override fun keyDown(keyCode: Int) : Boolean
    {
        val playerInput = directionGestureListener.playerInput

        when(keyCode)
        {
            Input.Keys.W, Input.Keys.UP ->
                playerInput.move(0.0f, -1.0f)
            Input.Keys.S, Input.Keys.DOWN ->
                playerInput.move(0.0f, 1.0f)
            Input.Keys.A, Input.Keys.LEFT ->
                playerInput.move(-1.0f, 0.0f)
            Input.Keys.D, Input.Keys.RIGHT ->
                playerInput.move(1.0f, 0.0f)
        }

        return super.keyDown(keyCode);
    }

    class DirectionGestureListener(private val maze: MazeActor,
                                         private val playerId: Int,
                                         private val camera: ThirdPersonCamera)
    : GestureAdapter()
    {
        val playerInput = PlayerInputAdjuster(maze, playerId, camera)

        override fun fling(x: Float, y: Float, button: Int) : Boolean
        {
            if(Gdx.app.getType() != Application.ApplicationType.Android) return false
            if(Gdx.input.isTouched(1)) return false

            playerInput.move(x, y)
            return false
        }
    }
}
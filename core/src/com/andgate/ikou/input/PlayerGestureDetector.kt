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

import com.andgate.ikou.actor.Scene
import com.andgate.ikou.actor.camera.CameraActor
import com.andgate.ikou.actor.maze.messages.MovePlayerMessage
import com.andgate.ikou.utility.math.ScreenSpaceTranslator
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.input.GestureDetector

class PlayerGestureDetector(val directionGestureListener: DirectionGestureListener)
: GestureDetector(directionGestureListener)
{
    private val TAG: String = "PlayerDirectionGestureDetector"

    override fun keyDown(keyCode: Int) : Boolean
    {
        when(keyCode)
        {
            Input.Keys.W, Input.Keys.UP ->
                directionGestureListener.movePlayer(0.0f, -1.0f)
            Input.Keys.S, Input.Keys.DOWN ->
                directionGestureListener.movePlayer(0.0f, 1.0f)
            Input.Keys.A, Input.Keys.LEFT ->
                directionGestureListener.movePlayer(-1.0f, 0.0f)
            Input.Keys.D, Input.Keys.RIGHT ->
                directionGestureListener.movePlayer(1.0f, 0.0f)
        }

        return super.keyDown(keyCode);
    }

    class DirectionGestureListener(val scene: Scene,
                                   val playerId: String)
    : GestureAdapter()
    {
        val trans = ScreenSpaceTranslator()
        val camera = scene.actors["camera"] as CameraActor

        override fun fling(x: Float, y: Float, button: Int) : Boolean
        {
            if(Gdx.app.getType() != Application.ApplicationType.Android) return false
            if(Gdx.input.isTouched(1)) return false

            movePlayer(x, y)

            return false
        }

        fun movePlayer(x: Float, y: Float)
        {
            val dir = trans.toMaxDirection(x, y, camera.angleX)
            scene.dispatcher.push("MovePlayer", MovePlayerMessage(dir, playerId))
        }
    }
}
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
import com.andgate.ikou.command.maze.MovePlayer
import com.andgate.ikou.graphics.camera.ThirdPersonCamera
import com.badlogic.gdx.math.Vector2

class PlayerInputAdjuster(private val maze: MazeActor,
                          private val playerId: Int,
                          private val camera: ThirdPersonCamera)
{


    fun move(velX: Float, velY: Float)
    {
        adjust(velX, velY)
        maze.cmd_proc.accept(MovePlayer(maze, dir.cpy(), playerId))
    }

    // Vectors are (kinda) expensive to create, so create some temp vectors
    private var vel = Vector2()
    private var dir = Vector2()
    private fun adjust(velX: Float, velY: Float)
    {
        vel.set(velX, velY)
        dir.set(0f,0f)
        vel.rotate(-camera.angleX)

        val absVelX = Math.abs(vel.x)
        val absVelY = Math.abs(vel.y)
        if(absVelX > absVelY)
        {
            dir.x = -vel.x / absVelX
        }
        else if (absVelX < absVelY)
        {
            dir.y = -vel.y / absVelY
        }
    }
}

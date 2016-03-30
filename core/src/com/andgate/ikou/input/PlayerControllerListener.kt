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
import com.andgate.ikou.input.mappings.OuyaPad
import com.andgate.ikou.input.mappings.Xbox360Pad
import com.andgate.ikou.graphics.camera.ThirdPersonCamera
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter

 class PlayerControllerListener(private val maze: MazeActor,
                                private val playerId: Int,
                                private val camera: ThirdPersonCamera)
 : ControllerAdapter()
{
    private val playerInput = PlayerInputAdjuster(maze, playerId, camera)

    private var xAxisLeft: Int = -1
    private var yAxisLeft: Int = -1

    private var xAxisValue: Float = 0.0f
    private var yAxisValue: Float = 0.0f

    fun update(delta_time: Float)
    {
        if(!(xAxisValue == 0f && yAxisValue == 0f))
        {
            playerInput.move(xAxisValue, yAxisValue)
            xAxisValue = 0.0f
            yAxisValue = 0.0f
        }
    }

    override fun axisMoved(controller: Controller, axisIndex: Int, value: Float): Boolean
    {
        mapToController(controller)

        val validValue: Float = if (Math.abs(value) >= 0.9f) value else 0f

        if(axisIndex == xAxisLeft)
        {
            xAxisValue = validValue
        }
        else if(axisIndex == yAxisLeft)
        {
            yAxisValue = validValue
        }

        return false;
    }

    private fun mapToController(controller: Controller)
    {
        if(Xbox360Pad.isXbox360Controller(controller))
        {
            xAxisLeft = Xbox360Pad.AXIS_LEFT_X
            yAxisLeft = Xbox360Pad.AXIS_LEFT_Y
        }
        else if(OuyaPad.isOuyaController(controller))
        {
            xAxisLeft = OuyaPad.AXIS_LEFT_X
            yAxisLeft = OuyaPad.AXIS_LEFT_Y
        }
        else
        {
            xAxisLeft = -1
            yAxisLeft = -1
        }
    }
}

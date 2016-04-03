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

package com.andgate.ikou.utility.math

import com.badlogic.gdx.math.Vector2

class ScreenSpaceTranslator
{
    private var vel = Vector2()
    private var dir = Vector2()

    fun toMaxDirection(velX: Float, velY: Float, cam_angleX: Float): Vector2
    {
        vel.set(velX, velY)
        dir.set(0f,0f)
        vel.rotate(-cam_angleX)

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

        return dir.cpy()
    }
}

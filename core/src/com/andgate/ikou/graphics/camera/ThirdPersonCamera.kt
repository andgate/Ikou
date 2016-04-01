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

package com.andgate.ikou.graphics.camera

import com.andgate.ikou.Constants
import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.player.PlayerPositionObserver.PlayerPositionListener
import com.andgate.ikou.constants.*
import com.andgate.ikou.utility.MathExtra
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3

class ThirdPersonCamera(val player: PlayerActor)
: PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, Gdx.graphics.getWidth().toFloat(), Gdx.graphics.getHeight().toFloat())
, PlayerPositionListener
{
    private val TAG: String = "ThirdPersonCamera";

    var angleX: Float = 0.0f
        private set
    var angleY: Float = 0.0f
        private set

    private val target = Vector3()
    private val tmpV1 = Vector3()

    init
    {
        player.pos_observer.add(this)

        target.set(player.pos)
        target.x += TILE_HALF_SPAN
        target.z += TILE_HALF_SPAN

        super.position.set( target.x,
                            target.y + Constants.CAMERA_VERTICAL_DISTANCE,
                            target.z - Constants.CAMERA_HORIZONTAL_DISTANCE)

        super.lookAt(target)
        super.near = 1f
        super.far = Constants.CAMERA_FAR
        super.update()
    }

    fun resize(viewportWidth: Int, viewportHeight: Int)
    {
        super.viewportWidth = viewportWidth.toFloat()
        super.viewportHeight = viewportHeight.toFloat()
        super.update(true)
    }

    fun zoom(amount: Float)
    {
        val currentDistance: Float = super.position.dst(player.pos)

        var displacement: Float = amount * PINCH_ZOOM_FACTOR
        val newDistance: Float = currentDistance - displacement

        if(!MathExtra.inRangeExclusive(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE))
        {
            val bound: Float = MathExtra.pickClosestBound(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE)
            displacement = currentDistance - bound
        }

        super.translate(tmpV1.set(super.direction).scl(displacement))
        super.update()
    }

    private val FULL_ROTATION_ANGLE: Float = -360f
    fun rotateFromDrag(deltaX: Float, deltaY: Float)
    {
        val deltaAngleX: Float = FULL_ROTATION_ANGLE * deltaX / Gdx.graphics.getWidth()
        val deltaAngleY: Float = FULL_ROTATION_ANGLE * deltaY / Gdx.graphics.getHeight()
        rotate(deltaAngleX, deltaAngleY)
    }

    fun rotate(deltaAngleX: Float, deltaAngleY: Float)
    {
        if(deltaAngleX == 0.0f && deltaAngleY == 0.0f) return
        var finalAngleY: Float = deltaAngleY
        val finalAngleX: Float = deltaAngleX

        tmpV1.set(super.direction).crs(super.up).y = 0f
        angleX += deltaAngleX

        var tmpAngleY: Float = angleY + deltaAngleY
        if(!MathExtra.inRangeInclusive(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX))
        {
            tmpAngleY = MathExtra.pickClosestBound(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX)
            finalAngleY = tmpAngleY - angleY
        }
        angleY = tmpAngleY

        super.rotateAround(target, tmpV1.nor(), finalAngleY)
        super.rotateAround(target, Vector3.Y, finalAngleX)
        super.update()
    }

    override fun playerPositionModified(dx: Float, dy: Float, dz: Float)
    {
        super.translate(dx, dy, dz)
        target.add(dx, dy, dz)
        super.update()
    }
}

package com.andgate.ikou.actor.camera

import com.andgate.ikou.Constants
import com.andgate.ikou.actor.Actor
import com.andgate.ikou.actor.Scene
import com.andgate.ikou.actor.camera.commands.TranslateCameraCommand
import com.andgate.ikou.actor.messaging.Message
import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.player.messages.PlayerMessage
import com.andgate.ikou.actor.player.messages.PlayerPositionChangeMessage
import com.andgate.ikou.constants.*
import com.andgate.ikou.graphics.player.PlayerModel
import com.andgate.ikou.utility.MathExtra
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.Vector3

class CameraActor(id: String,
                  scene: Scene,
                  val playerId: String)
: Actor(id, scene)
{
    private val TAG: String = "CameraActor";

    val player = scene.actors[playerId] as PlayerActor
    val cam = PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, Gdx.graphics.getWidth().toFloat(), Gdx.graphics.getHeight().toFloat())

    var angleX = 0f
        private set
    var angleY: Float = 0.0f
        private set

    init {
        scene.dispatcher.subscribe("PlayerPositionChanged", channel)
        channel.bind("PlayerPositionChanged", { msg ->
            val msg = msg as PlayerPositionChangeMessage
            cmd_proc.accept(TranslateCameraCommand(this, msg.dx, msg.dy, msg.dz))
        })
    }

    override fun receive(event: Message)
    {

    }

    override fun dispose()
    {
        super.dispose()
    }


    val target = Vector3()
    private val tmpV1 = Vector3()

    init
    {
        //player.pos_dispatcher.register(this)

        target.set(player.pos)
        target.x += TILE_HALF_SPAN
        target.z += TILE_HALF_SPAN

        cam.position.set( target.x,
                target.y + Constants.CAMERA_VERTICAL_DISTANCE,
                target.z - Constants.CAMERA_HORIZONTAL_DISTANCE)

        cam.lookAt(target)
        cam.near = 1f
        cam.far = Constants.CAMERA_FAR
        cam.update()
    }

    fun resize(viewportWidth: Int, viewportHeight: Int)
    {
        cam.viewportWidth = viewportWidth.toFloat()
        cam.viewportHeight = viewportHeight.toFloat()
        cam.update(true)
    }

    fun zoom(amount: Float)
    {
        val currentDistance: Float = cam.position.dst(player.pos)

        var displacement: Float = amount * PINCH_ZOOM_FACTOR
        val newDistance: Float = currentDistance - displacement

        if(!MathExtra.inRangeExclusive(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE))
        {
            val bound: Float = MathExtra.pickClosestBound(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE)
            displacement = currentDistance - bound
        }

        cam.translate(tmpV1.set(cam.direction).scl(displacement))
        cam.update()
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

        tmpV1.set(cam.direction).crs(cam.up).y = 0f
        angleX += deltaAngleX

        var tmpAngleY: Float = angleY + deltaAngleY
        if(!MathExtra.inRangeInclusive(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX))
        {
            tmpAngleY = MathExtra.pickClosestBound(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX)
            finalAngleY = tmpAngleY - angleY
        }
        angleY = tmpAngleY

        cam.rotateAround(target, tmpV1.nor(), finalAngleY)
        cam.rotateAround(target, Vector3.Y, finalAngleX)
        cam.update()
    }
}

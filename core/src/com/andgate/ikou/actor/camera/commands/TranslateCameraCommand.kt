package com.andgate.ikou.actor.camera.commands

import com.andgate.ikou.actor.Command
import com.andgate.ikou.actor.camera.CameraActor

class TranslateCameraCommand(camActor: CameraActor,
                             val dx: Float,
                             val dy: Float,
                             val dz: Float)
: CameraCommand(camActor)
{
    private val TAG: String = "TranslateCameraCommand"

    override fun execute()
    {
        camActor.cam.translate(dx, dy, dz);
        camActor.target.add(dx, dy, dz);
        camActor.cam.update();
    }
}
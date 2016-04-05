package com.andgate.ikou.actor.camera.commands

import com.andgate.ikou.actor.camera.CameraActor

class TranslateCameraCommand(camActor: CameraActor,
                             val x: Float,
                             val y: Float,
                             val z: Float)
: CameraCommand(camActor)
{
    private val TAG: String = "TranslateCameraCommand"

    override fun execute()
    {
        with(camActor)
        {
            camActor.cam.translate(x - target.x, y - target.y, z - target.z);
            camActor.target.set(x, y, z);
            camActor.cam.update();
        }
    }
}
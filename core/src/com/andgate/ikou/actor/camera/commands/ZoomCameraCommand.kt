package com.andgate.ikou.actor.camera.commands

import com.andgate.ikou.actor.camera.CameraActor

class ZoomCameraCommand(camActor: CameraActor,
                        val amount: Float)
: CameraCommand(camActor)
{
    override fun execute() {
    }
}
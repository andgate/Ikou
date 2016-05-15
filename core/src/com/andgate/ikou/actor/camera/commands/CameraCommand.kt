package com.andgate.ikou.actor.camera.commands

import com.andgate.ikou.actor.Command
import com.andgate.ikou.actor.camera.CameraActor

abstract class CameraCommand(val camActor: CameraActor) : Command()
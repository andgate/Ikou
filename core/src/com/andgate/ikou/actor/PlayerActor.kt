package com.andgate.ikou.actor;

import com.andgate.ikou.Ikou;
import com.andgate.ikou.command.CommandProcessor

class PlayerActor(game: Ikou) {
    val TAG: String = "PlayerActor"
    val cmd_proc = CommandProcessor(this)

    var x: Float = 0f
    var y: Float = 0f
    var z: Float = 0f

    fun update()
    {
        cmd_proc.update()
    }
}

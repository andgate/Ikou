package com.andgate.ikou.actor;

import com.andgate.ikou.Ikou;
import com.andgate.ikou.command.CommandProcessor
import com.andgate.ikou.graphics.player.PlayerModel
import com.badlogic.gdx.math.Vector3

class PlayerActor(val game: Ikou,
                  val model: PlayerModel)
{
    private val TAG: String = "PlayerActor"
    val cmd_proc = CommandProcessor()

    var pos = Vector3()

    fun update(delta_time: Float)
    {
        cmd_proc.update(delta_time)
    }
}

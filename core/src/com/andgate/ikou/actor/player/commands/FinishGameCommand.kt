package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor

class FinishGameCommand(player: PlayerActor) : PlayerCommand(player)
{
    override fun begin() {
        throw UnsupportedOperationException()
    }

    override fun step(delta_time: Float) {
        throw UnsupportedOperationException()
    }

    override fun end() {
        throw UnsupportedOperationException()
    }
}
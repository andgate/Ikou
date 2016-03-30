package com.andgate.ikou.command.player

import com.andgate.ikou.actor.PlayerActor

class Drop(player: PlayerActor) : PlayerCommand(player)
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

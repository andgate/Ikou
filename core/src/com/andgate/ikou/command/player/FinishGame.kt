package com.andgate.ikou.command.player

import com.andgate.ikou.actor.PlayerActor

class FinishGame : PlayerCommand()
{
    override fun execute(target: PlayerActor) {
        throw UnsupportedOperationException()
    }
}
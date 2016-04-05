package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor

class FinishGameCommand(player: PlayerActor) : PlayerCommand(player) {
    override fun execute() {
        throw UnsupportedOperationException()
    }
}
package com.andgate.ikou.command.player

import com.andgate.ikou.actor.player.PlayerActor

class HitEdge(player: PlayerActor) : PlayerCommand(player)
{
    override fun begin() {}

    override fun step(deltaTime: Float) {
        player.game.hitSound.play(0.5f)
        finished = true
    }

    override fun end() {}
}

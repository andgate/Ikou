package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor

class HitEdgeCommand(player: PlayerActor) : PlayerCommand(player)
{
    override fun begin() {
        player.scene.game.hitSound.play(0.5f)
    }

    override fun step(delta_time: Float) {
        finished = true
    }

    override fun end() {}
}

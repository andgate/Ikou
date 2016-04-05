package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor

class HitEdgeCommand(player: PlayerActor) : PlayerCommand(player)
{
    override fun execute() {
        player.scene.game.hitSound.play(0.5f)
    }
}

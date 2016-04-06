package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor

class HitEdgeCommand(player: PlayerActor) : PlayerCommand(player)
{
    override fun execute() {
        if(player.animator.tweens.isNotEmpty()) {
            val tween = player.animator.tweens.last
            val old_hook = tween.finish_hook
            tween.finish_hook = {
                old_hook()
                player.scene.game.hitSound.play(0.5f)
            }
        } else       player.scene.game.hitSound.play(0.5f)
    }
}

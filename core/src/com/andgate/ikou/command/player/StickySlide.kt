package com.andgate.ikou.command.player

import com.andgate.ikou.actor.PlayerActor
import com.badlogic.gdx.math.Vector3

class StickySlide(player: PlayerActor, val end_post: Vector3)
: PlayerCommand(player)
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

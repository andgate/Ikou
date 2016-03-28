package com.andgate.ikou.command.player

import com.andgate.ikou.actor.PlayerActor

class StickySlide(val y: Int, val x: Int, val z: Int) : PlayerCommand()
{
    override fun execute(target: PlayerActor) {
        throw UnsupportedOperationException()
    }
}

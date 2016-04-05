package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.constants.ROUGH_SLIDE_DECCELERATION
import com.andgate.ikou.constants.SLIDE_SPEED
import com.andgate.ikou.animate.AcceleratedTween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class StickySlideCommand(player: PlayerActor,
                         val start: Vector3,
                         val end: Vector3)
: PlayerCommand(player)
{
    private val TAG: String = "StickySlide"

    override fun execute()
    {
        player.scene.game.roughSound.play(0.2f)
        player.animator.add(AcceleratedTween(start, end, SLIDE_SPEED, ROUGH_SLIDE_DECCELERATION))
    }
}

package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.player.messages.StickySlideMessage
import com.andgate.ikou.constants.ROUGH_SLIDE_DECCELERATION
import com.andgate.ikou.constants.SLIDE_SPEED
import com.andgate.ikou.utility.AcceleratedTween
import com.andgate.ikou.utility.LinearTween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class StickySlideCommand(player: PlayerActor,
                         val end_pos: Vector3,
                         val muted: Boolean = false)
: PlayerCommand(player)
{
    private val TAG: String = "StickySlide"

    val tween = AcceleratedTween()

    override fun begin()
    {
        tween.setup(player.pos, end_pos, SLIDE_SPEED, ROUGH_SLIDE_DECCELERATION)
        if(!muted) player.scene.game.roughSound.play(0.2f)
    }

    override fun step(delta_time: Float)
    {
        finished = tween.update(delta_time)

        val pos = tween.get()
        Gdx.app.debug(TAG, "(x,y): (${pos.x}, ${pos.y})")
        player.pos = pos
    }

    override fun end() {}
}

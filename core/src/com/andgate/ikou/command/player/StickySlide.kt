package com.andgate.ikou.command.player

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.constants.ROUGH_SLIDE_DECCELERATION
import com.andgate.ikou.constants.SLIDE_SPEED
import com.andgate.ikou.utility.AcceleratedTween
import com.andgate.ikou.utility.LinearTween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class StickySlide(player: PlayerActor, val end_pos: Vector3)
: PlayerCommand(player)
{
    private val TAG: String = "StickySlide"

    val tween = AcceleratedTween()

    override fun begin()
    {
        tween.setup(player.pos, end_pos, SLIDE_SPEED, ROUGH_SLIDE_DECCELERATION)
        player.game.roughSound.play(0.2f)

        player.cmd_proc.nuke_buffer()
        player.cmd_proc.rejectAll()
    }

    override fun step(delta_time: Float)
    {
        finished = tween.update(delta_time)

        val pos = tween.get()
        Gdx.app.debug(TAG, "(x,y): (${pos.x}, ${pos.y})")
        player.pos = pos
    }

    override fun end() {
        player.cmd_proc.acceptAll()
    }
}

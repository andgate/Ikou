package com.andgate.ikou.command.player

import com.andgate.ikou.actor.PlayerActor
import com.andgate.ikou.constants.SLIDE_SPEED;
import com.andgate.ikou.utility.LinearTween
import com.badlogic.gdx.math.Vector3

class SmoothSlide(player: PlayerActor, val end_pos: Vector3)
: PlayerCommand(player)
{
    val tween = LinearTween()

    override fun begin()
    {
        tween.setup(player.pos, end_pos, SLIDE_SPEED)
    }

    override fun step(delta_time: Float)
    {
        finished = tween.update(delta_time)
        // set the position

        if(finished)
            residual_delta_time = tween.getLeftOverTime()
    }

    override fun end() {}
}
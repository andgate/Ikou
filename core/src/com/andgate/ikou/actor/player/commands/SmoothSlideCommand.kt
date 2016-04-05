package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.player.messages.SmoothSlideMessage
import com.andgate.ikou.constants.SLIDE_SPEED;
import com.andgate.ikou.animate.LinearTween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class SmoothSlideCommand(player: PlayerActor,
                         val start: Vector3,
                         val end: Vector3)
: PlayerCommand(player)
{
    private val TAG: String = "SmoothSlide"

    override fun execute()
    {
        player.animator.add(LinearTween(start, end, SLIDE_SPEED))
    }
}
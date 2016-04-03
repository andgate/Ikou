package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.constants.*
import com.andgate.ikou.utility.AcceleratedTween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class DropDownCommand(player: PlayerActor)
: PlayerCommand(player)
{
    private val TAG: String = "DropPlayerCommand"

    val tween = AcceleratedTween()

    override fun begin()
    {
        val end_pos = Vector3(player.pos)
        end_pos.y -= FLOOR_SPACING
        tween.setup(player.pos, end_pos, FALL_SPEED_INITIAL, FALL_ACCELERATION)
        player.scene.game.roughSound.play(0.2f)
    }

    override fun step(delta_time: Float)
    {
        finished = tween.update(delta_time)

        val pos = tween.get()
        Gdx.app.debug(TAG, "(x,y): (${pos.x}, ${pos.y})")
        player.pos = pos
    }

    override fun end()
    {
    }
}

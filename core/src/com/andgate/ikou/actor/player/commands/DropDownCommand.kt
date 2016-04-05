package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.constants.*
import com.andgate.ikou.animate.AcceleratedTween
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class DropDownCommand(player: PlayerActor,
                      val start: Vector3,
                      val end: Vector3)
: PlayerCommand(player)
{
    private val TAG: String = "DropPlayerCommand"

    override fun execute()
    {
        val drop_pos = Vector3(player.pos)
        drop_pos.y -= FLOOR_SPACING
        player.animator.add(AcceleratedTween(start, end, SLIDE_SPEED, ROUGH_SLIDE_DECCELERATION))
        player.animator.add(AcceleratedTween(end, drop_pos, FALL_SPEED_INITIAL, FALL_ACCELERATION))


        // at the start of the second animation, needs to play this sound
        player.scene.game.fallSound.play(0.5f)
        // At the end of the second animation, needs to play this sound
        // player.scene.game.hitSound.play()
    }
}

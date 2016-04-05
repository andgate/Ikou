package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.player.messages.PlayerPositionChangeMessage
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
        val slideTween = AcceleratedTween(start, end, SLIDE_SPEED, ROUGH_SLIDE_DECCELERATION)
        slideTween.update_hook = { pos -> player.scene.dispatcher.push(PlayerPositionChangeMessage(player.id, pos.x, pos.y, pos.z)) }
        player.animator.add(slideTween)


        val drop_pos = Vector3(end)
        drop_pos.y -= FLOOR_SPACING

        val dropTween = AcceleratedTween(end, drop_pos, FALL_SPEED_INITIAL, FALL_ACCELERATION)
        dropTween.start_hook = { player.scene.game.fallSound.play(0.5f) }
        dropTween.update_hook = { pos -> player.scene.dispatcher.push(PlayerPositionChangeMessage(player.id, pos.x, pos.y, pos.z)) }
        dropTween.finish_hook = { player.scene.game.hitSound.play() }
        player.animator.add(dropTween)
    }
}

package com.andgate.ikou.actor.player.messages

import com.badlogic.gdx.math.Vector3

class StickySlideMessage(playerId: String,
                       val end_pos: Vector3,
                       val muted: Boolean = false)
: PlayerMessage("StickySlide", playerId) {
}
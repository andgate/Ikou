package com.andgate.ikou.actor.player.messages

import com.badlogic.gdx.math.Vector3

class StickySlideMessage(playerId: String,
                         val start: Vector3,
                         val end: Vector3,
                         val muted: Boolean = false)
: PlayerMessage("StickySlide", playerId) {
}
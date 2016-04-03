package com.andgate.ikou.actor.player.messages

import com.badlogic.gdx.math.Vector3

class SmoothSlideMessage(playerId: String,
                       val end_pos: Vector3)
: PlayerMessage("SmoothSlide", playerId) {}
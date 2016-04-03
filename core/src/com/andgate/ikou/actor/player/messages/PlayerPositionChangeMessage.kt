package com.andgate.ikou.actor.player.messages

class PlayerPositionChangeMessage(playerId: String,
                                val dx: Float,
                                val dy: Float,
                                val dz: Float)
: PlayerMessage("PlayerPositionChanged", playerId)
{}
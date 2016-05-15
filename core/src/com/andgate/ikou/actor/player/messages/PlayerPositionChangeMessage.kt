package com.andgate.ikou.actor.player.messages

class PlayerPositionChangeMessage(playerId: String,
                                val x: Float,
                                val y: Float,
                                val z: Float)
: PlayerMessage("PlayerPositionChanged", playerId)
{}
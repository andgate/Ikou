package com.andgate.ikou.actor.player.messages

class FinishGameMessage(playerId: String)
: PlayerMessage("FinishGame", playerId) {}
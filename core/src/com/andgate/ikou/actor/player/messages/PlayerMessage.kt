package com.andgate.ikou.actor.player.messages

import com.andgate.ikou.actor.messaging.Message

open class PlayerMessage(id: String, val playerId: String)
: Message(id) {}
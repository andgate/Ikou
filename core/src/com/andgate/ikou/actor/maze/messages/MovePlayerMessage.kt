package com.andgate.ikou.actor.maze.messages

import com.andgate.ikou.actor.messaging.Message
import com.badlogic.gdx.math.Vector2

class MovePlayerMessage(val dir: Vector2,
                      val playerId: String)
: Message("MovePlayer")
{}
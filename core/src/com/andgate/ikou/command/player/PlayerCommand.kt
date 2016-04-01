package com.andgate.ikou.command.player

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.command.Command

abstract class PlayerCommand(val player: PlayerActor) : Command()

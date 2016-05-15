package com.andgate.ikou.actor.player.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.Command

abstract class PlayerCommand(val player: PlayerActor) : Command()

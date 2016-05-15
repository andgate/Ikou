package com.andgate.ikou.actor.player.messages

import com.badlogic.gdx.math.Vector3

class DropDownMessage(playerId: String,
                      val start: Vector3,
                      val end: Vector3)
: PlayerMessage("DropDown", playerId)
{
}
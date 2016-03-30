package com.andgate.ikou.actor

import com.andgate.ikou.Ikou
import com.andgate.ikou.command.CommandProcessor
import com.andgate.ikou.model.Tile
import com.badlogic.gdx.math.Vector3
import java.util.*


class MazeActor(game: Ikou,
                var tiles: HashMap<Vector3, Tile>, // [y][x][z] where y is the floor id, x is the row, and z is the column
                var players: Array<PlayerActor>)
{
    val TAG: String = "MazeActor"
    val cmd_proc = CommandProcessor()

    fun update(delta_time: Float)
    {
        cmd_proc.update(delta_time)
    }
}

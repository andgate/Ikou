package com.andgate.ikou.actor

import com.andgate.ikou.Ikou
import com.andgate.ikou.command.CommandProcessor
import com.andgate.ikou.graphics.maze.MazeModel
import com.andgate.ikou.maze.Tile
import com.andgate.ikou.maze.TileMap
import com.badlogic.gdx.math.Vector3
import java.util.*


class MazeActor(game: Ikou,
                var map: TileMap, // [y][x][z] where y is the floor id, x is the row, and z is the column
                var players: Array<PlayerActor>,
                var model: MazeModel)
{
    val TAG: String = "MazeActor"
    val cmd_proc = CommandProcessor()

    fun update(delta_time: Float)
    {
        cmd_proc.update(delta_time)
    }
}

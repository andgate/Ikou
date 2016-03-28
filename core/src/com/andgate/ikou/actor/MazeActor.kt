package com.andgate.ikou.actor;

import com.andgate.ikou.Ikou;
import com.andgate.ikou.command.CommandProcessor
import com.andgate.ikou.command.maze.MazeCommand;
import com.andgate.ikou.model.Tile;

import java.util.HashMap;
import java.util.Vector;

class MazeActor(game: Ikou,
                var tiles: Array<Array<Array<Tile>>>, // [y][x][z] where y is the floor id, x is the row, and z is the column
                var players: Array<PlayerActor>)
{
    val TAG: String = "MazeActor"
    val cmd_proc = CommandProcessor(this)

    fun update()
    {
        cmd_proc.update()
    }
}

package com.andgate.ikou.actor

import com.andgate.ikou.Ikou
import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.command.CommandProcessor
import com.andgate.ikou.graphics.maze.MazeModel
import com.andgate.ikou.maze.Maze
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import java.util.*


class MazeActor(game: Ikou,
                var maze: Maze, // [y][x][z] where y is the floor id, x is the row, and z is the column
                var players: Array<PlayerActor>,
                var model: MazeModel)
: Disposable
{
    val TAG: String = "MazeActor"
    val cmd_proc = CommandProcessor()
    val seed_phrase = ""

    fun update(delta_time: Float)
    {
        cmd_proc.update(delta_time)
    }

    override fun dispose()
    {
        model.dispose()
    }
}

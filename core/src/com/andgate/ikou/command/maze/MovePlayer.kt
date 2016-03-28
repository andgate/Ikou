package com.andgate.ikou.command.maze;

import com.andgate.ikou.actor.PlayerActor;
import com.andgate.ikou.command.maze.MazeCommand;
import com.andgate.ikou.actor.MazeActor;
import com.andgate.ikou.command.player.*
import com.andgate.ikou.model.Tile;
import com.andgate.ikou.utility.Vector2i


class MovePlayer(val dir : Direction,
                 val playerId: Int)
: MazeCommand()
{
    enum class Direction(val y: Int, val x: Int, val z: Int) {
        UP(1, 0, 0),
        DOWN(-1, 0, 0),
        LEFT(0, -1, 0),
        RIGHT(0, 1, 0),
        FORWARD(0, 0, 1),
        BACKWARD(0, 0, -1)
    }

    override fun execute(maze: MazeActor)
    {
        val player: PlayerActor = maze.players[playerId]

        var curr_y: Int = player.y.toInt() + dir.y;
        var curr_x: Int = player.x.toInt() + dir.x;
        var curr_z: Int = player.z.toInt() + dir.z;

        // Process the command
        while(!this.finished) {
            // This could throw an array out of bounds I guess?
            val next_tile: Tile = maze.tiles[curr_y][curr_x][curr_z];

            if (next_tile != null) {
                when (next_tile.type) {
                    Tile.Type.SMOOTH ->
                        player.cmd_proc.accept(SmoothSlide(curr_y, curr_x, curr_z))
                    Tile.Type.STICKY -> {
                        player.cmd_proc.accept(StickySlide(curr_y, curr_x, curr_z))
                        this.finished = true
                    }
                    Tile.Type.OBSTACLE, Tile.Type.EMPTY -> {
                        player.cmd_proc.accept(HitEdge())
                        this.finished = true // Can't move
                    }
                    Tile.Type.DROP ->{
                        player.cmd_proc.accept(Drop())
                        this.finished = true // Can't move
                    }
                    Tile.Type.FINISH ->{
                        player.cmd_proc.accept(FinishGame())
                        this.finished = true // Can't move
                    }
                }
            }
            else
            {
                this.finished = true
            }

            curr_y += dir.y;
            curr_x += dir.x;
            curr_z += dir.z;
        }
    }
}

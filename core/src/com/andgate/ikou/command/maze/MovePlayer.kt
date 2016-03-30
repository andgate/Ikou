package com.andgate.ikou.command.maze

import com.andgate.ikou.actor.PlayerActor
import com.andgate.ikou.actor.MazeActor
import com.andgate.ikou.command.player.*
import com.andgate.ikou.model.Tile
import com.andgate.ikou.utility.Vector3i
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3


class MovePlayer(maze: MazeActor,
                 val dir: Vector2,
                 val playerId: Int)
: MazeCommand(maze)
{
    override fun begin() {
        throw UnsupportedOperationException()
    }

    override fun step(delta_time: Float)
    {
        val player: PlayerActor = maze.players[playerId]

        var next_pos = Vector3(player.pos.x + dir.x,
                               player.pos.y        ,
                               player.pos.x + dir.y)

        // Process the command
        while(!this.finished) {
            // This could throw an array out of bounds I guess?
            val next_tile: Tile? = maze.tiles[next_pos];

            when (next_tile?.type) {
                Tile.Type.SMOOTH ->
                    player.cmd_proc.accept(SmoothSlide(player, next_pos.cpy()))
                Tile.Type.STICKY -> {
                    player.cmd_proc.accept(StickySlide(player, next_pos.cpy()))
                    this.finished = true
                }
                Tile.Type.OBSTACLE, Tile.Type.EMPTY -> {
                    player.cmd_proc.accept(HitEdge(player))
                    this.finished = true // Can't move
                }
                Tile.Type.DROP ->{
                    player.cmd_proc.accept(Drop(player))
                    this.finished = true // Can't move
                }
                Tile.Type.FINISH ->{
                    player.cmd_proc.accept(FinishGame(player))
                    this.finished = true // Can't move
                }
            }

            next_pos.x += dir.x;
            next_pos.z += dir.y;
        }
    }

    override fun end() {
        throw UnsupportedOperationException()
    }
}

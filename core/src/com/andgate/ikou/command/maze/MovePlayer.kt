package com.andgate.ikou.command.maze

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.MazeActor
import com.andgate.ikou.command.player.*
import com.andgate.ikou.constants.TILE_HEIGHT
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3


class MovePlayer(mazeActor: MazeActor,
                 val dir: Vector2,
                 val playerId: Int)
: MazeCommand(mazeActor)
{
    override fun begin() {}

    override fun step(delta_time: Float)
    {
        val player: PlayerActor = mazeActor.players[playerId]

        // Current tile position of the player
        val curr_pos = player.pos
        var next_pos = Vector3(curr_pos.x + dir.x,
                               curr_pos.y, // Player is always a tile's height above
                               curr_pos.z + dir.y)

        // Process the command
        while(!this.finished) {
            val next_tile: Tile? = mazeActor.maze.map[next_pos]

            if(next_tile == null) {
                player.cmd_proc.accept(HitEdge(player))
                this.finished = true
                return
            }

            when (next_tile.type) {
                Tile.Type.SMOOTH ->
                    player.cmd_proc.accept(SmoothSlide(player, next_pos.cpy()))
                Tile.Type.STICKY -> {
                    player.cmd_proc.accept(StickySlide(player, next_pos.cpy()))
                    this.finished = true
                }
                Tile.Type.OBSTACLE -> {
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

    override fun end() {}
}

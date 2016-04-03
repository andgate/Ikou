package com.andgate.ikou.actor.maze.commands

import com.andgate.ikou.actor.player.PlayerActor
import com.andgate.ikou.actor.maze.MazeActor
import com.andgate.ikou.actor.player.messages.*
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3


class MovePlayerCommand(mazeActor: MazeActor,
                        val dir: Vector2,
                        val playerId: String)
: MazeCommand(mazeActor)
{
    override fun begin() {}

    override fun step(delta_time: Float)
    {
        val player = mazeActor.scene.actors[playerId] as PlayerActor

        // If player is current executing commands,
        // just ignore this command
        if(player.cmd_proc.comm_buffer.isNotEmpty()) {
            finished = true
            return
        }

        // Current tile position of the player
        val curr_pos = player.pos
        var next_pos = Vector3(curr_pos.x + dir.x,
                curr_pos.y, // Player is always a tile's height above
                curr_pos.z + dir.y)

        // Process the command
        while(!this.finished) {
            val next_tile: Tile? = mazeActor.maze.map[next_pos]
            val dispatcher =  player.scene.dispatcher

            if(next_tile == null) {
                dispatcher.push(HitEdgeMessage(playerId))
                this.finished = true
                return
            }

            when (next_tile.type) {
                Tile.Type.SMOOTH -> {
                    dispatcher.push(SmoothSlideMessage(playerId, next_pos.cpy()))
                }
                Tile.Type.STICKY -> {
                    dispatcher.push(StickySlideMessage(playerId, next_pos.cpy()))
                    this.finished = true
                }
                Tile.Type.OBSTACLE -> {
                    dispatcher.push(HitEdgeMessage(playerId))
                    this.finished = true // Can't move
                }
                Tile.Type.DROP ->{
                    dispatcher.push(StickySlideMessage(playerId, next_pos.cpy(), true))
                    dispatcher.push(DropDownMessage(playerId))
                    this.finished = true // Can't move
                }
                Tile.Type.FINISH ->{
                    dispatcher.push(FinishGameMessage(playerId))
                    this.finished = true // Can't move
                }
            }

            next_pos.x += dir.x;
            next_pos.z += dir.y;
        }
    }

    override fun end() {}
}

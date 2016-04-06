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
    override fun execute()
    {
        val player = mazeActor.scene.actors[playerId] as PlayerActor

        // If player is current animated, or the direction is (0,0)
        // just ignore this command
        if(player.animator.tweens.isNotEmpty() || dir.isZero) {
            return
        }

        // Current tile position of the player
        var curr_pos = Vector3()
        player.model.transform.getTranslation(curr_pos)
        var next_pos = Vector3(curr_pos).add(dir.x, 0f, dir.y)

        // Just keep processing until a return statement is hit
        while(true) {
            val next_tile: Tile? = mazeActor.maze.map[next_pos]
            val dispatcher =  player.scene.dispatcher

            if(next_tile == null) {
                dispatcher.push(HitEdgeMessage(playerId))
                return
            }

            when (next_tile.type) {
                Tile.Type.SMOOTH -> {
                    dispatcher.push(SmoothSlideMessage(playerId, curr_pos.cpy(), next_pos.cpy()))
                }
                Tile.Type.STICKY -> {
                    dispatcher.push(StickySlideMessage(playerId, curr_pos.cpy(), next_pos.cpy()))
                    return
                }
                Tile.Type.OBSTACLE -> {
                    dispatcher.push(HitEdgeMessage(playerId))
                    return
                }
                Tile.Type.DROP ->{
                    dispatcher.push(DropDownMessage(playerId, curr_pos.cpy(), next_pos.cpy()))
                    return
                }
                Tile.Type.FINISH ->{
                    dispatcher.push(FinishGameMessage(playerId))
                    return
                }
            }

            curr_pos.set(next_pos)
            next_pos.add(dir.x, 0f, dir.y)
        }
    }
}

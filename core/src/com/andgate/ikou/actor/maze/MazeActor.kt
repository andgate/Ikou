package com.andgate.ikou.actor.maze

import com.andgate.ikou.actor.Actor
import com.andgate.ikou.actor.messaging.*
import com.andgate.ikou.actor.Scene
import com.andgate.ikou.actor.maze.commands.MovePlayerCommand
import com.andgate.ikou.actor.maze.messages.MovePlayerMessage
import com.andgate.ikou.graphics.maze.MazeModel
import com.andgate.ikou.maze.Maze


class MazeActor(id: String,
                scene: Scene,
                var maze: Maze, // [y][x][z] where y is the floor id, x is the row, and z is the column
                var model: MazeModel)
: Actor(id, scene)
{
    val TAG: String = "MazeActor"
    val seed_phrase = ""

    init {
        scene.dispatcher.subscribe("MovePlayer", channel)
        channel.bind("MovePlayer", { msg ->
            val msg = msg as MovePlayerMessage
            cmd_proc.accept(MovePlayerCommand(this, msg.dir, msg.playerId))
        })
    }

    override fun receive(event: Message) {}

    override fun update(delta_time: Float) {}

    override fun dispose()
    {
        super.dispose()
        model.dispose()
    }
}

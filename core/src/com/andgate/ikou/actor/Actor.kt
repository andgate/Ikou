package com.andgate.ikou.actor

import com.andgate.ikou.actor.messaging.Channel
import com.andgate.ikou.actor.messaging.Message
import com.badlogic.gdx.utils.Disposable

abstract class Actor(val id: String, val scene: Scene) : Disposable {
    val cmd_proc = CommandProcessor()
    val channel = Channel()

    init {
        scene.actors.put(id, this)
    }

    abstract fun receive(event: Message)

    fun process_events()
    {
        for(msg in channel.queue)
        {
            receive(msg)
        }
        channel.clear()
    }

    fun process_commands(delta_time: Float)
    {
        cmd_proc.update(delta_time)
    }

    open override fun dispose()
    {
        scene.actors.remove(id)
    }
}
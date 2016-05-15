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

    abstract fun update(delta_time: Float)

    abstract fun receive(event: Message)

    fun process_events()
    {
        channel.queue.map{ receive(it) }
        channel.clear()
    }

    open override fun dispose()
    {
        scene.actors.remove(id)
    }
}
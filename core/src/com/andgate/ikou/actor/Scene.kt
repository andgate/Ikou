package com.andgate.ikou.actor

import com.andgate.ikou.Ikou
import com.andgate.ikou.actor.messaging.Dispatcher
import java.util.*

class Scene(val game: Ikou) {
    val actors = LinkedHashMap<String, Actor>()
    val dispatcher = Dispatcher()

    fun update(delta_time: Float)
    {
        process_events()
        process_commands(delta_time)
    }

    private fun process_events()
    {
        for((id, actor) in actors)
        {
            actor.process_events()
        }
    }

    private fun process_commands(delta_time: Float)
    {
        for((id, actor) in actors)
        {
            actor.process_commands(delta_time)
        }
    }
}
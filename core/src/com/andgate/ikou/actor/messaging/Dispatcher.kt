package com.andgate.ikou.actor.messaging

import java.util.*

class Dispatcher {
    // Event subscribers
    val channels = HashMap<String, Vector<Channel>>()

    fun subscribe(chan_id: String, chan: Channel)
    {
        if(channels.containsKey(chan_id)) {
            channels[chan_id]!!.add(chan)
        } else {
            channels.put(chan_id, Vector())
            channels[chan_id]!!.add(chan)
        }
    }

    fun unsubscribe(chan_id: String, chan: Channel)
    {
        if(channels.containsKey(chan_id))
            channels[chan_id]!!.remove(chan)
    }

    fun push(chan_id: String, event: Message)
    {
        if(channels.containsKey(chan_id))
        {
            for(channel in channels[chan_id]!!)
            {
                channel.push(event)
            }
        }
    }

    fun push(event: Message)
    {
        push(event.id, event)
    }
}
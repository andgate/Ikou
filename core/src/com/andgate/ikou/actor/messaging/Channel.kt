package com.andgate.ikou.actor.messaging

import java.util.*


class Channel {
    val queue = LinkedList<Message>()
    val receivers = LinkedHashMap<String, LinkedList<(Message) -> Unit>>()

    fun push(msg: Message) {
        if(receivers.containsKey(msg.id)) {
            for(receiver in receivers[msg.id]!!) {
                receiver(msg)
            }
        } else {
            queue.addLast(msg)
        }
    }

    // Bind a receiver function to event.
    // When event enters the channel, it will be passed into this function
    // instead of the queue. Multiple receivers can be bound to a single event.
    // This allows for synchronous event handling.
    fun bind(msg_id: String, receiver: (Message) -> Unit)
    {
        if(!receivers.containsKey(msg_id))
            receivers.put(msg_id, LinkedList())

        receivers[msg_id]!!.addLast(receiver)
    }

    fun unbind(msg_id: String)
    {
        receivers.remove(msg_id)
    }

    fun clear()
    {
        queue.clear()
    }
}
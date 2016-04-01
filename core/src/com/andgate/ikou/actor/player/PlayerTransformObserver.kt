package com.andgate.ikou.actor.player

import java.util.*

class PlayerPositionObserver
{
    val listeners = Vector<PlayerPositionListener>()

    interface PlayerPositionListener
    {
        fun playerPositionModified(dx: Float, dy: Float, dz: Float)
    }

    fun add(listener: PlayerPositionListener)
    {
        listeners.add(listener);
    }

    fun remove(listener: PlayerPositionListener)
    {
        listeners.remove(listener);
    }

    fun notify(dx: Float, dy: Float, dz: Float)
    {
        for(listener in listeners)
        {
            listener.playerPositionModified(dx, dy, dz);
        }
    }
}
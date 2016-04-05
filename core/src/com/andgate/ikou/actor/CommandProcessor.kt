package com.andgate.ikou.actor

import java.util.*

class CommandProcessor
{
    private val TAG: String = "CommandProcessor"
    // Command history stores commands that were executed and are now finished
    var history = Vector<Command>()

    // Command buffer stores commands to be executed when the current command is finished
    var buffer = LinkedList<Command>()
    var is_accepting = true

    fun update()
    {
        buffer.map { it.execute() }
        history.addAll(buffer)
        buffer.clear()
    }

    fun accept(new_comm: Command)
    {
        if(is_accepting) buffer.add(new_comm)
    }

    fun nuke_history()
    {
        history.clear()
    }

    fun nuke_buffer()
    {
        buffer.clear()
    }

    fun rejectAll()
    {
        is_accepting = false
    }

    fun acceptAll()
    {
        is_accepting = true
    }
}
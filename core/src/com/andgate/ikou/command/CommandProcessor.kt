package com.andgate.ikou.command

import java.util.*

class CommandProcessor
{
    // Command history stores commands that were executed and are now finished
    private var comm_history = Vector<Command>()

    // Command buffer stores commands to be executed when the current command is finished
    private var comm_buffer = Vector<Command>()
    private var is_accepting = true

    // The command that is currently being executed
    private var curr_comm: Command? = null

    fun update(delta_time: Float)
    {
        // Execute, throwing away null results
        curr_comm?.step(delta_time)

        // Check if finished
        if (curr_comm?.finished == true) {
            // If finished, add the command to history
            comm_history.add(curr_comm)
            curr_comm?.end()

            // If there is not a command in the buffer
            if (comm_buffer.isEmpty())
                curr_comm = null // set current command to null
            else {
                // Otherwise move the first command in the buffer to the current command
                val residual_delta_time = curr_comm?.residual_delta_time
                curr_comm = comm_buffer.removeAt(0)

                curr_comm?.begin()
                if(residual_delta_time != null)
                    curr_comm?.step(residual_delta_time)
            }
        }
    }

    fun accept(new_comm: Command)
    {
        if(!is_accepting) return

        // If there is no command to execute
        if(curr_comm == null) {
            curr_comm = new_comm
            curr_comm?.begin()
        }
        else // Otherwise, buffer the new command
            comm_buffer.add(new_comm)
    }

    fun nuke_history()
    {
        comm_history.clear()
    }

    fun nuke_buffer()
    {
        comm_buffer.clear()
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
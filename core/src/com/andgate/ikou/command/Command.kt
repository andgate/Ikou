package com.andgate.ikou.command

abstract class Command {
    var finished: Boolean = false
    var residual_delta_time: Float? = null

    abstract fun begin()
    abstract fun step(delta_time: Float)
    abstract fun end()
}

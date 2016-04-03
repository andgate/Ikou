package com.andgate.ikou.actor

abstract class Command {
    var finished: Boolean = false
    var residual_delta_time: Float? = null

    abstract fun begin()
    abstract fun step(delta_time: Float)
    abstract fun end()
}

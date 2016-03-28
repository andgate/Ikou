package com.andgate.ikou.command

abstract class Command<T> {
    var finished: Boolean = false

    abstract fun execute(target: T)
}

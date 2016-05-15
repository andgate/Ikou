package com.andgate.ikou.animate

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

abstract class Tween(val start: Vector3,
                     val end: Vector3)
{
    protected val curr = Vector3()
    protected var duration: Float = 0f

    var update_hook: (pos: Vector3) -> Unit = {}
    var start_hook: () -> Unit = {}
    var finish_hook: () -> Unit = {}

    var isCompleted: Boolean = false
        protected set

    abstract fun update(target: Matrix4, delta: Float)
    abstract fun remainingDelta(): Float
}
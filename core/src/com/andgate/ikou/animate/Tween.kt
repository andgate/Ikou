package com.andgate.ikou.animate

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

abstract class Tween(val start: Vector3,
                     val end: Vector3)
{
    protected val curr = Vector3()
    protected var duration: Float = 0f

    var isCompleted: Boolean = false
        protected set

    abstract fun update(target: Matrix4, delta: Float)
    abstract fun remainingDelta(): Float
}
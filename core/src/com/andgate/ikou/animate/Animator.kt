package com.andgate.ikou.animate

import com.badlogic.gdx.math.Matrix4
import java.util.*

class Animator(val target: Matrix4)
{
    val tweens = LinkedList<Tween>()

    fun update(delta_time: Float)
    {
        if(tweens.isEmpty() || delta_time == 0f) return

        val curr_tween = tweens.first()
        curr_tween.update(target, delta_time)
        if(curr_tween.isCompleted) {
            tweens.removeFirst()
            update(curr_tween.remainingDelta())
        }
    }

    fun add(tween: Tween)
    {
        tweens.addLast(tween)
    }
}
package com.andgate.ikou.animate;

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3

class AcceleratedTween(start: Vector3,
                       end: Vector3,
                       val speed: Float,
                       val accel: Float)
: Tween(start, end)
{
    private val TAG: String = "AcceleratedTween"

    private val displacement: Vector3 = Vector3()
    private val velocity: Vector3 = Vector3()
    private val acceleration: Vector3 = Vector3()

    init
    {
        displacement.set(end)
        displacement.sub(start)
        val dist: Float = displacement.len()

        velocity.set(displacement);
        velocity.scl(speed / dist);

        acceleration.set(displacement);
        acceleration.scl(accel / 2 / dist);

        // accel needs to have a direction or something??
        duration = calculateTweenTime(dist, speed, accel);
    }

    private var elapsed_time: Float = 0f

    fun getPercentComplete(): Float
    {
        return elapsed_time / duration;
    }

    /**
     * Update the tween. New value can be retrieved with LinearTween.get()
     * @param delta Time to elapse.
     * @return False if unfinished, true when done.
     */
    override fun update(target: Matrix4, delta: Float)
    {
        if(elapsed_time == 0f) start_hook()
        elapsed_time += delta;

        if(elapsed_time >= duration) {
            curr.set(end)
            isCompleted = true
            finish_hook()
        } else {
            curr.set(start)
            curr.add(velocity.cpy().scl(elapsed_time))
            curr.add(acceleration.cpy().scl(elapsed_time*elapsed_time))
        }

        target.setTranslation(curr)
        update_hook(curr.cpy())
    }

    private fun calculateTweenTime(dist: Float, vel: Float, accel: Float): Float
    {
        var time: Float = 0f

        if (accel == 0f)
        {
            time = dist / vel
        }
        else
        {
            val vel_final: Float = Math.sqrt((2f * accel * dist + vel * vel).toDouble()).toFloat()
            time = Math.abs((vel_final - vel) / accel)
        }

        return time
    }

    override fun remainingDelta(): Float
    {
        return 0f
    }
}

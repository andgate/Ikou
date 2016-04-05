/*
    This file is part of Ikou.
    Ikou is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License.
    Ikou is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with Ikou.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.andgate.ikou.animate;

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3;

class LinearTween(start: Vector3,
                  end: Vector3,
                  val speed: Float)
: Tween(start, end)
{
    private val TAG: String = "LinearTween"

    private val ACCUMULATOR_MAX: Float = 1f

    private var accumulator: Float = 0f

    init
    {
        duration = calculateTweenTime(start, end, speed)
    }

    /**
     * Update the tween. New value can be retrieved with LinearTween.get()
     * @param delta Time to elapse.
     * @return False if unfinished, true when done.
     */
    override fun update(target: Matrix4, delta: Float)
    {
        val percentTween: Float = delta / duration

        if(accumulator == 0f)
            start_hook()
        accumulator += percentTween

        if(accumulator >= ACCUMULATOR_MAX)
        {
            curr.set(end)
            isCompleted = true
            finish_hook()
        }
        else
        {
            curr.set(start)
            curr.lerp(end, accumulator)
        }

        target.setTranslation(curr)
        update_hook(curr.cpy())
    }

    private fun calculateTweenTime(start: Vector3, end: Vector3, speed: Float): Float
    {
        val distance = Vector3(end).sub(start)
        val time: Float = distance.len() / speed
        return time
    }

    // Get delta time unused by the tween, for chaining consecutive tweens.
    override fun remainingDelta(): Float
    {
        val deltaAccum: Float = accumulator - ACCUMULATOR_MAX
        if(deltaAccum > 0.0f)
        {
            val delta_time: Float = deltaAccum * duration
            return delta_time
        }

        return 0f
    }
}

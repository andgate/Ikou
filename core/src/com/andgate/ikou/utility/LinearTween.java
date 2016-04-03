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

package com.andgate.ikou.utility;

import com.badlogic.gdx.math.Vector3;

public class LinearTween
{
    private static final String TAG = "LinearTween";

    private static final boolean FINISHED = true;
    private static final boolean UNFINISHED = false;

    private Vector3 start = new Vector3();
    private Vector3 end = new Vector3();
    private Vector3 curr = new Vector3();
    private float duration = 0.0f;
    private float accumulator = 0.0f;

    private static final float ACCUMULATOR_MAX = 1.0f;

    public void setup(Vector3 start, Vector3 end, float speed)
    {
        this.start.set(start);
        this.end.set(end);
        this.duration = calculateTweenTime(start, end, speed);

        reset();
    }

    public void setup(float x1, float y1, float z1, float x2, float y2, float z2, float speed)
    {
        this.start.set(x1, y1, z1);
        this.end.set(x2, y2, z2);
        this.duration = calculateTweenTime(this.start, this.end, speed);

        reset();
    }

    /**
     * Update the tween. New value can be retrieved with LinearTween.get()
     * @param delta Time to elapse.
     * @return False if unfinished, true when done.
     */
    public boolean update(float delta)
    {
        float percentTween = delta / duration;
        accumulator += percentTween;

        if(accumulator >= ACCUMULATOR_MAX)
        {
            curr.set(end);
            return FINISHED;
        }
        else
        {
            curr.set(start);
            curr.lerp(end, accumulator);
        }

        return UNFINISHED;
    }

    public Vector3 get()
    {
        return curr.cpy();
    }

    public void reset()
    {
        accumulator = 0.0f;
    }

    private static Vector3 distance = new Vector3();
    private static float calculateTweenTime(Vector3 start, Vector3 end, float speed)
    {
        distance.set(end);
        distance.sub(start);

        float time = distance.len() / speed;

        return time;
    }

    // Get delta time unused by the tween, for chaining consecutive tweens.
    public float getLeftOverTime()
    {
        float deltaAccum = accumulator - ACCUMULATOR_MAX;
        if(deltaAccum > 0.0f)
        {
            float deltaTime = deltaAccum * duration;
            return deltaTime;
        }

        return 0.0f;
    }
}

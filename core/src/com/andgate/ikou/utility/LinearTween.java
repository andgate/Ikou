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

    public void setup(Vector3 start, Vector3 end, float speed)
    {
        this.start.set(start);
        this.end.set(end);
        this.duration = calculateTweenTime(start, end, speed);

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

        if(accumulator >= 1.0f)
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
        return curr;
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
}

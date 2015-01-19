package com.andgate.ikou.utility;

import com.badlogic.gdx.math.Vector3;

public class AcceleratedTween
{
    private static final String TAG = "AcceleratedTween";

    private static final boolean FINISHED = true;
    private static final boolean UNFINISHED = false;

    private Vector3 startPos = new Vector3();
    private Vector3 endPos = new Vector3();
    private Vector3 currPos = new Vector3();
    private float duration = 0.0f;


    private Vector3 displacement = new Vector3();
    private Vector3 velocity = new Vector3();
    private Vector3 acceleration = new Vector3();

    public void setup(Vector3 startPos, Vector3 endPos, float vel, float accel)
    {
        this.startPos.set(startPos);
        this.endPos.set(endPos);

        displacement.set(endPos);
        displacement.sub(startPos);
        float dist = displacement.len();

        velocity.set(displacement);
        velocity.scl(vel / dist);

        acceleration.set(displacement);
        acceleration.scl(accel / 2 / dist);

        // accel needs to have a direction or something??
        this.duration = calculateTweenTime(dist, vel, accel);

        reset();
    }

    private float t = 0.0f;

    public float getPercentComplete()
    {
        return t / duration;
    }

    /**
     * Update the tween. New value can be retrieved with LinearTween.get()
     * @param delta Time to elapse.
     * @return False if unfinished, true when done.
     */
    public boolean update(float delta)
    {
        t += delta;

        if(t >= duration)
        {
            currPos.set(endPos);
            return FINISHED;
        }
        else
        {
            currPos.set(startPos);
            currPos.add(velocity.x * t, velocity.y * t, velocity.z * t);
            currPos.add(acceleration.x * t * t, acceleration.y * t * t, acceleration.z * t * t);
        }

        return UNFINISHED;
    }

    public Vector3 get()
    {
        return currPos;
    }

    public void reset()
    {
        reset(false);
    }

    public void reset(boolean overflowLast)
    {
        if(overflowLast && t > duration)
        {
            t = t - duration;
        }
        else
        {
            t = 0.0f;
        }
    }

    private static float calculateTweenTime(float dist, float vel, float accel)
    {
        float time = 0;

        if (accel == 0)
        {
            time = dist / vel;
        }
        else
        {
            float vel_final = (float)Math.sqrt(2.0f * accel * dist + vel * vel);
            time = Math.abs((vel_final - vel) / accel);
        }

        return time;
    }
}

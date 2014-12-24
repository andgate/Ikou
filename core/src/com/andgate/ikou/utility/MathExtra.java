package com.andgate.ikou.utility;

import com.andgate.ikou.Constants;

/**
 * Created by Gabe on 12/24/2014.
 */
public class MathExtra {
    public static float pickClosestBound(float n, float low, float high)
    {
        float highDistance = Math.abs(high - n);
        float lowDistance = Math.abs(low - n);

        if(lowDistance < highDistance)
        {
            return low;
        }

        return high;
    }

    public static boolean epsilonEquals(float a, float b)
    {
        return inRangeInclusive(a, b - Constants.EPSILON, b + Constants.EPSILON);
    }

    public static boolean epsilonGT(float a, float b)
    {
        return ( a > (b - Constants.EPSILON) );
    }

    public static boolean inRangeInclusive(float n, float low, float high)
    {
        return (low <= n && n <= high);
    }

    public static boolean inRangeExclusive(float n, float low, float high)
    {
        return (low < n && n < high);
    }
}

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

import com.andgate.ikou.Constants;

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

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

package com.andgate.ikou.utility.graphics;

import com.andgate.ikou.utility.MathExtra;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class ColorUtils
{
    public static boolean isOpaque(Color color)
    {
        return MathExtra.epsilonEquals(color.a, 1.0f);
    }

    public static boolean isTransparent(Color color)
    {
        return ( isVisible(color) && (color.a < 1.0f) );
    }

    public static boolean isVisible(Color color)
    {
        return (color.a > 0.0f);
    }

    private static HSL tmpHSL1 = new HSL();
    private static HSL tmpHSL2 = new HSL();

    public static void generateRandomColor(float s, float l, Color out)
    {
        tmpHSL1.h = MathUtils.random(1.0f);
        tmpHSL1.s = s;
        tmpHSL1.l = l;

        tmpHSL1.toRGBA(out);
    }

    public static void tween(Color start, Color end, float percent, Color out)
    {
        tmpHSL1.fromRGBA(start);
        tmpHSL2.fromRGBA(end);

        tmpHSL1.h = tmpHSL1.h * (1.0f - percent) + tmpHSL2.h * percent;
        tmpHSL1.s = tmpHSL1.s * (1.0f - percent) + tmpHSL2.s * percent;
        tmpHSL1.l = tmpHSL1.l * (1.0f - percent) + tmpHSL2.l * percent;

        tmpHSL1.toRGBA(out);
    }
}

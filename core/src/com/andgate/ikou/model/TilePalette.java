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

package com.andgate.ikou.model;

import com.andgate.ikou.utility.graphics.ColorUtils;
import com.andgate.ikou.utility.graphics.HSL;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class TilePalette {
    public Color obstacle = new Color(Color.GRAY);
    public Color smooth = new Color(Color.LIGHT_GRAY);
    public Color rough = new Color(Color.DARK_GRAY);
    public Color player = new Color(Color.ORANGE);
    public Color end = new Color(Color.RED);
    public Color blank = new Color(Color.CLEAR);
    public Color background = new Color(Color.WHITE);

    public TilePalette()
    {
        defaultScheme();
    }

    public Color getColor(TileStack.Tile type)
    {
        switch(type)
        {
            case Smooth:
                return smooth;
            case Obstacle:
                return obstacle;
            case Rough:
                return rough;
            case Player:
                return player;
            case End:
                return end;
            case Victory:
                return background;
            default:
                return blank;
        }
    }

    private static HSL tmpHsl1 = new HSL();
    private static HSL tmpHsl2 = new HSL();

    public void defaultScheme()
    {
        tmpHsl1.h = 2.0f / 3.6f;
        tmpHsl1.s = 0.7f;
        tmpHsl1.l = 0.7f;
        tmpHsl1.toRGBA(smooth);

        tmpHsl1.l = 0.9f;
        tmpHsl1.toRGBA(rough);

        tmpHsl1.l = 0.5f;
        tmpHsl1.h = 2.0f / 3.0f;
        tmpHsl1.toRGBA(obstacle);

    }

    public void randomize()
    {
        ColorUtils.generateRandomColor(0.7f, 0.5f, player);

        tmpHsl1.fromRGBA(player);
        tmpHsl1.h = 1.0f - tmpHsl1.h;

        tmpHsl1.toRGBA(end);

        // Find a color for the level that isn't similar to the player or the end
        do
        {
            ColorUtils.generateRandomColor(0.5f, 0.5f, obstacle);
            tmpHsl2.fromRGBA(obstacle);
        } while (MathUtils.isEqual(tmpHsl1.h, tmpHsl2.h, 0.07f) || MathUtils.isEqual(1.0f - tmpHsl1.h, tmpHsl2.h, 0.07f));

        tmpHsl2.l = 0.7f;
        tmpHsl2.toRGBA(smooth);

        tmpHsl2.l = 0.2f;
        tmpHsl2.toRGBA(rough);

        ColorUtils.generateRandomColor(MathUtils.random(1.0f), MathUtils.random(1.0f), background);
    }
}

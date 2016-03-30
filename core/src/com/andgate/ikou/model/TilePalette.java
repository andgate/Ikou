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

import com.andgate.ikou.model.Tile;

import com.andgate.ikou.utility.graphics.ColorUtils;
import com.andgate.ikou.utility.graphics.HSL;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class TilePalette {
    public Color sticky = new Color(Color.DARK_GRAY);
    public Color smooth = new Color(Color.LIGHT_GRAY);
    public Color obstacle = new Color(Color.BLUE);
    public Color end = new Color(Color.RED);
    public Color background = new Color(Color.WHITE);

    public Color player = new Color(Color.ORANGE);

    public Color colorOf(Tile.Type type)
    {
        switch(type)
        {
            case SMOOTH:
                return smooth;
            case STICKY:
                return sticky;
            case OBSTACLE:
                return obstacle;
            case DROP:
                return player;
            case FINISH:
                return end;
            default:
                return null;
        }
    }
}

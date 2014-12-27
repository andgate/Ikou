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

package com.andgate.ikou.model.tile;

import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.tile.TileData.TileType;
import com.badlogic.gdx.graphics.Color;

public class TileFactory
{
    public static TileData build(TileType type, TilePalette palette)
    {
        Color color = getColor(type, palette);
        return new TileData(type, color);
    }

    public static Color getColor(TileType type, TilePalette palette)
    {
        Color color = new Color(Color.CLEAR);

        switch(type)
        {
            case Smooth:
                color.set(palette.smooth);
                break;
            case Obstacle:
                color.set(palette.obstacle);
                break;
            case Rough:
                color.set(palette.rough);
                break;
            case Player:
                color.set(palette.player);
                break;
            case End:
                color.set(palette.end);
                break;
            case Blank:
                color.set(palette.blank);
                break;
            default:
                break;
        }

        return color;
    }
}

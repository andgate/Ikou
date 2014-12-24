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

import com.andgate.ikou.Constants;
import com.andgate.ikou.utility.MathExtra;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;

public class TileData
{
    public enum TileType
    {
        Smooth, Obstacle, Rough, Player, End, Blank
    }

    public static final float WIDTH = Constants.TILE_LENGTH;
    public static final float HEIGHT = Constants.TILE_THICKNESS;
    public static final float DEPTH = Constants.TILE_LENGTH;

    public static final float HALF_WIDTH = WIDTH / 2.0f;
    public static final float HALF_HEIGHT = HEIGHT / 2.0f;
    public static final float HALF_DEPTH = DEPTH / 2.0f;

    public static final Material TILE_MATERIAL
            = new Material(
                  new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                //, new IntAttribute(IntAttribute.CullFace, GL20.GL_NONE)
            );

    private TileType type;
    protected Color color;

    public TileData()
    {
        this(TileType.Blank, Color.CLEAR);
    }

    public TileData(TileType type, Color color)
    {
        this.type = type;
        this.color = new Color(color);
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setType(TileType type)
    {
        this.type = type;
    }

    public TileType getType()
    {
        return type;
    }

    public boolean isOpaque()
    {
        return MathExtra.epsilonEquals(color.a, 1.0f);
    }

    public boolean isTransparent()
    {
        return ( isVisible() && (color.a < 1.0f) );
    }

    public boolean isVisible()
    {
        return (color.a > 0.0f);
    }
}

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

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.tile.TileCode;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;

public class TileStack
{
    public static final float WIDTH = Constants.TILE_LENGTH;
    public static final float HALF_WIDTH = WIDTH / 2.0f;
    public static final float HEIGHT = Constants.TILE_THICKNESS;
    public static final float HALF_HEIGHT = HEIGHT / 2.0f;
    public static final float DEPTH = Constants.TILE_LENGTH;
    public static final float HALF_DEPTH = DEPTH / 2.0f;

    public static final Material TILE_MATERIAL
            = new Material(
                  new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
                //, new IntAttribute(IntAttribute.CullFace, GL20.GL_NONE)
            );

    public enum Tile
    {
        Smooth, Obstacle, Rough, Player, End, Blank
    }

    Tile[] tiles;

    public TileStack()
    {
        this(Tile.Blank);
    }

    public TileStack(Tile tile)
    {
        this.tiles = buildTileStack(tile);
    }

    public TileStack(char code)
    {
        this(codeToTile(code));
    }

    public Tile[] getTiles()
    {
        return tiles;
    }

    public void setTiles(Tile[] tiles)
    {
        this.tiles = tiles;
    }

    public Tile get(int index)
    {
        if(index < tiles.length)
        {
            return tiles[index];
        }

        return Tile.Blank;
    }

    public int size()
    {
        return tiles.length;
    }

    private static Tile[] buildTileStack(Tile tile)
    {
        switch(tile)
        {
            case Smooth:
                return new Tile[]{ Tile.Smooth };
            case Obstacle:
                return new Tile[]{ Tile.Smooth, Tile.Obstacle };
            case Rough:
                return new Tile[]{ Tile.Rough };
            case End:
                return new Tile[]{ Tile.End };
            default:
                return new Tile[]{ Tile.Blank };
        }
    }

    private static Tile codeToTile(char tileCode)
    {
        switch(tileCode)
        {
            case TileCode.SMOOTH_TILE:
                return Tile.Smooth;
            case TileCode.OBSTACLE_TILE:
                return Tile.Obstacle;
            case TileCode.ROUGH_TILE:
                return Tile.Rough;
            case TileCode.END_TILE:
                return Tile.End;
            default:
                return Tile.Blank;
        }
    }
}

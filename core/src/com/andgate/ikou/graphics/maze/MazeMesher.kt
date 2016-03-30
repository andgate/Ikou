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

package com.andgate.ikou.graphics.maze;

import com.andgate.ikou.Constants;
import com.andgate.ikou.graphics.util.CubeMesher;
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.utility.graphics.ColorUtils;
import com.badlogic.gdx.graphics.Color;

class MazeMesher : CubeMesher()
{
    public MazeMesher()
    {
        Array2d<TileSector> sectors = masterSector.getSectors();
        TileSector sector = sectors.get(sectorRow, sectorColumn);
        TileStack[][] stacks = sector.getStacks();

        for (int z = 0; z < TileSector.SIZE; z++)
        {
            for (int x = 0; x < TileSector.SIZE; x++)
            {
                TileStack currTileStack = stacks[z][x];

                for (int y = 0; y < currTileStack.size(); y++)
                {
                    int masterX = sectorColumn * TileSector.SIZE + x;
                    int masterY = y;
                    int masterZ = sectorRow * TileSector.SIZE + z;

                    float xPos = (float) x * TileStack.WIDTH + offsetX;
                    float yPos = (float) y * TileStack.HEIGHT;
                    float zPos = (float) z * TileStack.DEPTH + offsetZ;

                    addVisibleQuads(masterSector, palette, masterX, masterY, masterZ, xPos, yPos, zPos);
                }
            }
        }
    }

    /**
     * Adds a culled tile to the mesh, culling non-visible sides.
     * @param masterSector The sector to fetch information from
     * @param x Used to fetch tile information
     * @param y Used to fetch tile information
     * @param z Used to fetch tile information
     * @param xPos Used to designate tile location
     * @param yPos Used to designate tile location
     * @param zPos Used to designate tile location
     */
    protected fun addVisibleQuads(MasterSector masterSector, TilePalette palette, int x, int y, int z, float xPos, float yPos, float zPos)
    {
        Tile tile = masterSector.get(x, y, z);
        Color tileColor = palette.getColor(tile);

        if(!ColorUtils.isVisible(tileColor))
            return;

        Color wallColor = palette.obstacle;

        if(y == 0 && tile != Tile.Blank)
        {
            addWalls(masterSector, wallColor, x, z, xPos, zPos);
        }

        calculateVerts(xPos, yPos, zPos);

        switch(tile)
        {
            case Smooth:
                addTop(palette.obstacle);
                addBottom(palette.obstacle);
                break;
            case Obstacle:
                break;
            case End:
                break;
            default:
                break;
        }

        if (!masterSector.doesTileExist(x, y + 1, z))
        {
            addTop(tileColor);
        }

        if (!masterSector.doesTileExist(x, y - 1, z))
        {
            if(tile == Tile.Smooth)
            {
                addBottom(palette.obstacle);
            }
            else
            {
                addBottom(tileColor);
            }
        }

        if(tile == Tile.Obstacle)
        {
            if (masterSector.doesTileExist(x, 0, z + 1)
                    && !masterSector.doesTileExist(x, 1, z + 1))
                addFront(tileColor);
            if (masterSector.doesTileExist(x, 0, z - 1)
                    && !masterSector.doesTileExist(x, 1, z - 1))
                addBack(tileColor);
            if (masterSector.doesTileExist(x - 1, 0, z)
                    && !masterSector.doesTileExist(x - 1, 1, z))
                addLeft(tileColor);
            if (masterSector.doesTileExist(x + 1, 0, z)
                    && !masterSector.doesTileExist(x + 1, 1, z))
                addRight(tileColor);
        }
    }

    private void addWalls(MasterSector masterSector, Color wallColor, int x, int z, float xPos, float zPos)
    {

        boolean isDualLayer = masterSector.doesTileExist(x, 1, z);

        if (!masterSector.doesTileExist(x, 0, z + 1))
        {
            WallCorner leftCorner = getFrontLeftCorner(masterSector, x, z);
            WallCorner rightCorner = getFrontRightCorner(masterSector, x, z);
            addFrontWall(leftCorner, rightCorner, wallColor, isDualLayer, xPos, zPos);
        }
        if (!masterSector.doesTileExist(x, 0, z - 1))
        {
            WallCorner leftCorner = getBackLeftCorner(masterSector, x, z);
            WallCorner rightCorner = getBackRightCorner(masterSector, x, z);
            addBackWall(leftCorner, rightCorner, wallColor, isDualLayer, xPos, zPos);
        }
        if (!masterSector.doesTileExist(x - 1, 0, z))
        {
            WallCorner frontCorner = getFrontLeftCorner(masterSector, x, z);
            WallCorner backCorner = getBackLeftCorner(masterSector, x, z);
            addLeftWall(frontCorner, backCorner, wallColor, isDualLayer, xPos, zPos);
        }
        if (!masterSector.doesTileExist(x + 1, 0, z))
        {
            WallCorner frontCorner = getFrontRightCorner(masterSector, x, z);
            WallCorner backCorner = getBackRightCorner(masterSector, x, z);
            addRightWall(frontCorner, backCorner, wallColor, isDualLayer, xPos, zPos);
        }
    }

    // This gets REALLY ugly. I'm not proud of it.
    // Unfortunantly, there are just too many little differences for
    // each wall they each one needed it's own, special routine.
    // The worst part about it, is the routines are very similar.

    private enum WallCorner {None, Inside, Outside }

    private WallCorner getFrontLeftCorner(MasterSector masterSector, int x, int z)
    {
        WallCorner corner = WallCorner.Outside;

        if(masterSector.doesTileExist(x-1, 0, z+1))
        {
            corner = WallCorner.Inside;
        }
        else if(masterSector.doesTileExist(x-1, 0, z))
        {
            corner = WallCorner.None;
        }

        return corner;
    }

    private WallCorner getFrontRightCorner(MasterSector masterSector, int x, int z)
    {
        WallCorner corner = WallCorner.Outside;

        if(masterSector.doesTileExist(x+1, 0, z+1))
        {
            corner = WallCorner.Inside;
        }
        else if(masterSector.doesTileExist(x+1, 0, z))
        {
            corner = WallCorner.None;
        }

        return corner;
    }

    private WallCorner getBackLeftCorner(MasterSector masterSector, int x, int z)
    {
        WallCorner corner = WallCorner.Outside;

        if(masterSector.doesTileExist(x-1, 0, z-1))
        {
            corner = WallCorner.Inside;
        }
        else if(masterSector.doesTileExist(x-1, 0, z))
        {
            corner = WallCorner.None;
        }

        return corner;
    }

    private WallCorner getBackRightCorner(MasterSector masterSector, int x, int z)
    {
        WallCorner corner = WallCorner.Outside;

        if(masterSector.doesTileExist(x+1, 0, z-1))
        {
            corner = WallCorner.Inside;
        }
        else if(masterSector.doesTileExist(x+1, 0, z))
        {
            corner = WallCorner.None;
        }

        return corner;
    }

    private void addFrontWall(WallCorner leftCorner, WallCorner rightCorner, Color color, boolean isDualLayer, final float x, final float z)
    {
        float width = Constants.TILE_LENGTH;
        float height = Constants.WALL_HEIGHT;
        float depth = Constants.WALL_THICKNESS;
        float xPos = x;
        float yPos = 0;
        float zPos = z + Constants.TILE_LENGTH;

        switch(leftCorner)
        {
            case Inside:
                width -= Constants.WALL_THICKNESS;
                xPos += Constants.WALL_THICKNESS;
                break;
            case Outside:
                width += Constants.WALL_THICKNESS;
                xPos -= Constants.WALL_THICKNESS;
                break;
            default:
                break;
        }

        switch(rightCorner)
        {
            case Inside:
                width -= Constants.WALL_THICKNESS;
                break;
            case Outside:
                width += Constants.WALL_THICKNESS;
                break;
            default:
                break;
        }

        calculateVerts(xPos, yPos, zPos, width, height, depth);

        addTop(color);
        addBottom(color);
        addFront(color);

        if(rightCorner == WallCorner.Outside)
            addRight(color);

        if(leftCorner == WallCorner.Outside)
            addLeft(color);

        if(!isDualLayer)
        {
            xPos = x;
            width = Constants.TILE_LENGTH;
            height /= 2.0f;
            yPos = height;
            calculateVerts(xPos, yPos, zPos, width, height, depth);
            addBack(color);
        }
    }

    private void addBackWall(WallCorner leftCorner, WallCorner rightCorner, Color color, boolean isDualLayer, float x, float z)
    {
        float width = Constants.TILE_LENGTH;
        float height = Constants.WALL_HEIGHT;
        float depth = Constants.WALL_THICKNESS;
        float xPos = x;
        float yPos = 0;
        float zPos = z - depth;

        switch(leftCorner)
        {
            case Inside:
                width -= Constants.WALL_THICKNESS;
                xPos += Constants.WALL_THICKNESS;
                break;
            case Outside:
                width += Constants.WALL_THICKNESS;
                xPos -= Constants.WALL_THICKNESS;
                break;
            default:
                break;
        }

        switch(rightCorner)
        {
            case Inside:
                width -= Constants.WALL_THICKNESS;
                break;
            case Outside:
                width += Constants.WALL_THICKNESS;
                break;
            default:
                break;
        }

        calculateVerts(xPos, yPos, zPos, width, height, depth);

        addTop(color);
        addBottom(color);
        addBack(color);

        if(rightCorner == WallCorner.Outside)
            addRight(color);

        if(leftCorner == WallCorner.Outside)
            addLeft(color);

        if(!isDualLayer)
        {
            xPos = x;
            width = Constants.TILE_LENGTH;
            height /= 2.0f;
            yPos = height;
            calculateVerts(xPos, yPos, zPos, width, height, depth);
            addFront(color);
        }
    }

    private void addLeftWall(WallCorner frontCorner, WallCorner backCorner, Color color, boolean isDualLayer, float x, float z)
    {
        float depth = Constants.TILE_LENGTH;
        float width = Constants.WALL_THICKNESS;
        float height = Constants.WALL_HEIGHT;
        float xPos = x - width;
        float yPos = 0;
        float zPos = z;

        calculateVerts(xPos, yPos, zPos, width, height, depth);

        addTop(color);
        addBottom(color);

        if(frontCorner == WallCorner.Inside)
        {
            addFront(color);
            depth -= Constants.WALL_THICKNESS;
        }

        if(backCorner == WallCorner.Inside)
        {
            addBack(color);
            depth -= Constants.WALL_THICKNESS;
            zPos += Constants.WALL_THICKNESS;
        }

        calculateVerts(xPos, yPos, zPos, width, height, depth);
        addLeft(color);

        if(!isDualLayer)
        {
            height /= 2.0f;
            depth = Constants.TILE_LENGTH;
            yPos = height;
            zPos = z;
            calculateVerts(xPos, yPos, zPos, width, height, depth);
            addRight(color);
        }
    }

    private void addRightWall(WallCorner frontCorner, WallCorner backCorner, Color color, boolean isDualLayer, float x, float z)
    {
        float width = Constants.WALL_THICKNESS;
        float depth = Constants.TILE_LENGTH;
        float height = Constants.WALL_HEIGHT;
        float xPos = x + Constants.TILE_LENGTH;
        float yPos = 0;
        float zPos = z;

        calculateVerts(xPos, yPos, zPos, width, height, depth);

        addTop(color);
        addBottom(color);

        if(frontCorner == WallCorner.Inside)
        {
            addFront(color);
            depth -= Constants.WALL_THICKNESS;
        }

        if(backCorner == WallCorner.Inside)
        {
            addBack(color);
            depth -= Constants.WALL_THICKNESS;
            zPos += Constants.WALL_THICKNESS;
        }

        calculateVerts(xPos, yPos, zPos, width, height, depth);
        addRight(color);

        if(!isDualLayer)
        {
            height /= 2.0f;
            depth = Constants.TILE_LENGTH;
            yPos = height;
            zPos = z;
            calculateVerts(xPos, yPos, zPos, width, height, depth);
            addLeft(color);
        }
    }
}

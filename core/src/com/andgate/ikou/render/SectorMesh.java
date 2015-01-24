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

package com.andgate.ikou.render;

import com.andgate.ikou.model.MasterSector;
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileSector;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.utility.Array2d;
import com.andgate.ikou.utility.graphics.ColorUtils;
import com.badlogic.gdx.graphics.Color;

public class SectorMesh extends TileMesh
{
    public SectorMesh(MasterSector masterSector, int sectorRow, int sectorColumn, TilePalette palette, int offsetX, int offsetZ)
    {
        super();

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

                    addCulledTile(masterSector, palette, masterX, masterY, masterZ, xPos, yPos, zPos);
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
    public void addCulledTile(MasterSector masterSector, TilePalette palette, int x, int y, int z, float xPos, float yPos, float zPos)
    {
        Tile tile = masterSector.get(x, y, z);
        Color tileColor = palette.getColor(tile);

        if(tile == null || !ColorUtils.isVisible(tileColor))
            return;

        calculateVerts(xPos, yPos, zPos);

        boolean isDualLayer = masterSector.isTileVisible(tileColor, x, y + 1, z)
                || masterSector.isTileVisible(tileColor, x, y - 1, z);

        if (!masterSector.isTileVisible(tileColor, x, y, z + 1))
        {
            //addFront(tileColor);
            addFrontWall(isDualLayer);
        }
        if (!masterSector.isTileVisible(tileColor, x, y, z - 1))
        {
            //addBack(tileColor);
            addBackWall(isDualLayer);
        }
        if (!masterSector.isTileVisible(tileColor, x - 1, y, z))
        {
            //addLeft(tileColor);
            addLeftWall(isDualLayer);
        }
        if (!masterSector.isTileVisible(tileColor, x + 1, y, z))
        {
            //addRight(tileColor);
            addRightWall(isDualLayer);
        }
        if (!masterSector.isTileVisible(tileColor, x, y + 1, z))
        {
            addTop(tileColor);
        }
        if (!masterSector.isTileVisible(tileColor, x, y - 1, z))
        {
            // Could say palette.Smooth, but
            // this is only gonna come up for smooth tiles,
            // or maybe even end tiles.
            addBottom(tileColor);
        }
    }

    private void addFrontWall(boolean isDualLayer)
    {
    }

    private void addBackWall(boolean isDualLayer)
    {

    }

    private void addLeftWall(boolean isDualLayer)
    {

    }

    private void addRightWall(boolean isDualLayer)
    {

    }
}

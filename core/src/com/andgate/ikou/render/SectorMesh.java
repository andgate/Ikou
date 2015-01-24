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
        this(masterSector, sectorRow, sectorColumn, palette, offsetX, offsetZ, true);
    }

    public SectorMesh(MasterSector masterSector, int sectorRow, int sectorColumn, TilePalette palette, int offsetX, int offsetZ, boolean cullFaces)
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
                    Tile currTile = currTileStack.get(y);
                    Color currTileColor = palette.getColor(currTile);

                    int masterX = sectorColumn * TileSector.SIZE + x;
                    int masterY = y;
                    int masterZ = sectorRow * TileSector.SIZE + z;

                    if (ColorUtils.isVisible(currTileColor))
                    {
                        float xPos = (float) x * TileStack.WIDTH + offsetX;
                        float yPos = (float) y * TileStack.HEIGHT;
                        float zPos = (float) z * TileStack.DEPTH + offsetZ;

                        if(cullFaces && ColorUtils.isOpaque(currTileColor))
                        {
                            addCulledTile(masterSector, currTileColor, masterX, masterY, masterZ, xPos, yPos, zPos);
                        }
                        else
                        {
                            addTile(currTileColor, xPos, yPos, zPos);
                        }
                    }
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
    public void addCulledTile(MasterSector masterSector, Color tileColor, int x, int y, int z, float xPos, float yPos, float zPos)
    {
        Tile tile = masterSector.get(x, y, z);

        if(tile == null || !ColorUtils.isVisible(tileColor))
            return;

        calculateVerts(xPos, yPos, zPos);

        if (!masterSector.isTileVisible(tileColor, x, y, z + 1))
            addFront(tileColor);
        if (!masterSector.isTileVisible(tileColor, x, y, z - 1))
            addBack(tileColor);
        if (!masterSector.isTileVisible(tileColor, x - 1, y, z))
            addLeft(tileColor);
        if (!masterSector.isTileVisible(tileColor, x + 1, y, z))
            addRight(tileColor);
        if (!masterSector.isTileVisible(tileColor, x, y + 1, z))
            addTop(tileColor);
        if (!masterSector.isTileVisible(tileColor, x, y - 1, z))
            addBottom(tileColor);
    }
}

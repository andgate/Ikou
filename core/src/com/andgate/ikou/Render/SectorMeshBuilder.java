package com.andgate.ikou.Render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Tiles.TileData;
import com.andgate.ikou.Tiles.TileSector;
import com.andgate.ikou.Tiles.TileStack;
import com.badlogic.gdx.utils.Array;

public class SectorMeshBuilder extends TileMeshBuilder
{
    public SectorMeshBuilder(TileSector sector)
    {
        this(sector, 0, 0, true);
    }

    public SectorMeshBuilder(TileSector sector, int offsetX, int offsetZ)
    {
        this(sector, offsetX, offsetZ, true);
    }

    public SectorMeshBuilder(TileSector sector, int offsetX, int offsetZ, boolean cullFaces)
    {
        super();

        for (int z = 0; z < sector.size; z++)
        {
            Array<TileStack> currSectorRow = sector.get(z);

            for (int x = 0; x < currSectorRow.size; x++)
            {
                TileStack currTileStack = currSectorRow.get(x);

                for (int y = 0; y < currTileStack.size; y++)
                {
                    TileData currTile = currTileStack.get(y);

                    if (currTile.isVisible())
                    {
                        float xPos = (float) x * TileData.WIDTH + offsetX;
                        float yPos = (float) y * TileData.HEIGHT;
                        float zPos = (float) z * TileData.DEPTH + offsetZ;

                        if(cullFaces)
                        {
                            addCulledTile(sector, x, y, z, xPos, yPos, zPos);
                        }
                        else
                        {
                            addTile(currTile, xPos, yPos, zPos);
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds a culled tile to the mesh, culling non-visible sides.
     * @param sector The sector to fetch information from
     * @param x Used to fetch tile information
     * @param y Used to fetch tile information
     * @param z Used to fetch tile information
     * @param xPos Used to designate tile location
     * @param yPos Used to designate tile location
     * @param zPos Used to designate tile location
     */
    public void addCulledTile(TileSector sector, int x, int y, int z, float xPos, float yPos, float zPos)
    {
        TileData tile = sector.getTile(x, y, z);

        if(tile == null || !tile.isVisible())
            return;

        calculateVerts(xPos, yPos, zPos);

        if (!sector.isTileVisible(x, y, z + 1))
            addFront(tile);
        if (!sector.isTileVisible(x, y, z - 1))
            addBack(tile);
        if (!sector.isTileVisible(x - 1, y, z))
            addLeft(tile);
        if (!sector.isTileVisible(x + 1, y, z))
            addRight(tile);
        if (!sector.isTileVisible(x, y + 1, z))
            addTop(tile);
        if (!sector.isTileVisible(x, y - 1, z))
            addBottom(tile);
    }
}

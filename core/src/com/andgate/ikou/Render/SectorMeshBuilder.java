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
        this(sector, true);
    }

    public SectorMeshBuilder(TileSector sector, boolean cullFaces)
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
                        float xPos = (float) x * TileData.WIDTH;
                        float yPos = (float) y * TileData.HEIGHT;
                        float zPos = (float) z * TileData.DEPTH;

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

    public void addCulledTile(TileSector sector, int x, int y, int z, float xPos, float yPos, float zPos)
    {
        TileData tile = sector.getTile(x, y, z);

        if(tile == null)
            return;

        calculateVerts(xPos, yPos, zPos);

        if (!sector.doesTileExist(x, y, z + 1))
            addFront(tile);
        if (!sector.doesTileExist(x, y, z - 1))
            addBack(tile);
        if (!sector.doesTileExist(x - 1, y, z))
            addLeft(tile);
        if (!sector.doesTileExist(x + 1, y, z))
            addRight(tile);
        if (!sector.doesTileExist(x, y + 1, z))
            addTop(tile);
        if (!sector.doesTileExist(x, y - 1, z))
            addBottom(tile);
    }
}

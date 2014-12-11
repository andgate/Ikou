package com.andgate.ikou.Tiles;

import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Utility.Array2d;
import com.badlogic.gdx.utils.Array;

public class TileSector extends Array2d<TileStack>
{
    public TileSector(TileMaze maze)
    {
        super();

        char[][] tiles = maze.getTiles();

        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[i].length; j++)
            {
                addToRow(new TileStack(tiles[i][j]));
            }

            addRow();
        }
    }

    public boolean doesTileExist(int x, int y, int z)
    {
        if(isInArray(z, size))
        {
            Array<TileStack> sectorRow = get(z);
            if(isInArray(x, sectorRow.size))
            {
                TileStack tileStack = sectorRow.get(x);
                if(isInArray(y, tileStack.size))
                {
                    TileData tile = tileStack.get(y);
                    return tile.isVisible;
                }
            }
        }

        return false;
    }

    public TileData getTile(int x, int y, int z)
    {
        if(doesTileExist(x, y, z))
        {
            Array<TileStack> sectorRow = get(z);
            TileStack tileStack = sectorRow.get(x);
            return tileStack.get(y);
        }

        return null;
    }

    public int countTiles()
    {
        int count = 0;

        for (int z = 0; z < this.size; z++)
        {
            Array<TileStack> currSectorRow = this.get(z);

            for (int x = 0; x < currSectorRow.size; x++)
            {
                TileStack currTileStack = currSectorRow.get(x);

                for (int y = 0; y < currTileStack.size; y++)
                {
                    //TileData currTile = currTileStack.get(y);

                    count++;
                }
            }
        }

        return count;
    }


    private boolean isInArray(int n, int size)
    {
        return (0 <= n && n < size);
    }
}

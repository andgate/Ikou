package com.andgate.ikou.model;

import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.utility.Array2d;
import com.badlogic.gdx.utils.Array;

public class TileSector extends Array2d<TileStack>
{
    public TileSector()
    {
        super();
    }

    public boolean doesTileExist(int x, int y, int z)
    {
        if(isInArray(z, size))
        {
            Array<TileStack> sectorRow = get(z);
            if(isInArray(x, sectorRow.size))
            {
                TileStack tileStack = sectorRow.get(x);
                return isInArray(y, tileStack.size);
            }
        }

        return false;
    }

    public boolean doesTileStackExist(int x, int z)
    {
        if(isInArray(z, size))
        {
            Array<TileStack> sectorRow = get(z);
            return isInArray(x, sectorRow.size);
        }

        return false;
    }

    public boolean isTileVisible(int x, int y, int z)
    {
        TileData tile = getTile(x, y, z);
        if(tile != null)
        {
            return tile.isVisible();
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

    public TileStack getTileStack(int x, int z)
    {
        if(doesTileStackExist(x, z))
        {
            Array<TileStack> sectorRow = get(z);
            return sectorRow.get(x);
        }

        return null;
    }

    public TileSector[][] split(int startRow, int startColumn, int size)
    {
        int endRow = (int) Math.ceil((float)countRows() / size);
        int endColumn = (int) Math.ceil((float)maxColumns() / size);

        TileSector[][] subsectors = new TileSector[endRow][endColumn];

        for(int row = startRow; row < endRow; row++)
        {
            for(int column = startColumn; column < endColumn; column++)
            {
                int sectorRow = startRow + row * size;
                int sectorColumn = startColumn + column * size;
                subsectors[row][column] = getSubsector(sectorRow, sectorColumn, size);
            }
        }

        return subsectors;
    }

    public TileSector getSubsector(int startRow, int startColumn, int size)
    {
        TileSector subsector = new TileSector();

        int endRow = size + startRow;
        int endColumn = size + startColumn;

        for(int currRow = startRow; currRow < endRow; currRow++)
        {
            for(int currColumn = startColumn; currColumn < endColumn; currColumn++)
            {
                TileStack currStack = getTileStack(currColumn, currRow);

                if(doesTileStackExist(currColumn, currRow))
                {
                    subsector.addToRow(currStack);
                }
                else
                {
                    // end the loop here
                    currColumn = endColumn;
                }
            }

            if(isInArray(currRow + 1, this.size))
            {
                subsector.addRow();
            }
            else
            {
                currRow = endRow;
            }
        }

        return subsector;
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

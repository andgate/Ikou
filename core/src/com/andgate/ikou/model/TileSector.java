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

import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.utility.graphics.ColorUtils;
import com.badlogic.gdx.graphics.Color;

public class TileSector
{
    private static final String TAG = "TileSector";

    public static final int SIZE = 16;

    private TileStack[][] stacks;

    public TileSector()
    {
        stacks = new TileStack[TileSector.SIZE][TileSector.SIZE];

        for(int i = 0; i < TileSector.SIZE; i++)
        {
            for(int j = 0; j < TileSector.SIZE; j++)
            {
                stacks[i][j] = new TileStack(Tile.Blank);
            }
        }
    }

    public void set(TileStack stack, int row, int column)
    {
        stacks[row][column] = stack;
    }

    public boolean doesTileExist(int x, int y, int z)
    {
        if(isInArray(z, stacks.length))
        {
            TileStack[] stacksRow = stacks[z];
            if(isInArray(x, stacksRow.length))
            {
                TileStack stack = stacksRow[x];
                if(isInArray(y, stack.size()))
                {
                    Tile tile = stack.get(y);
                    return ((tile != Tile.Blank) && (tile != null));
                }
            }
        }

        return false;
    }

    public boolean doesTileStackExist(int x, int z)
    {
        if(isInArray(z, stacks.length))
        {
            TileStack[] stacksRow = stacks[x];
            return isInArray(x, stacksRow.length);
        }

        return false;
    }

    public boolean isTileVisible(Color color, int x, int y, int z)
    {
        Tile tile = getTile(x, y, z);
        if(tile != null)
        {
            return ColorUtils.isVisible(color);
        }

        return false;
    }

    public Tile getTile(int x, int y, int z)
    {
        if(doesTileExist(x, y, z))
        {
            TileStack[] stacksRow = stacks[z];
            TileStack stack = stacksRow[x];
            return stack.get(y);
        }

        return null;
    }

    public TileStack getTileStack(int x, int z)
    {
        if(doesTileStackExist(x, z))
        {
            TileStack[] stacksRow = stacks[z];
            return stacksRow[x];
        }

        return null;
    }

    public TileStack[][] getStacks()
    {
        return stacks;
    }

    // This used to be very useful for subdividing a tilesector into a master tilesector.
    // Now that there is a specific master tilesector object for this,
    // split is much less useful and revelevant.
    /**public TileSector[][] split(int startRow, int startColumn, int size)
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
    }**/

    private boolean isInArray(int n, int size)
    {
        return (0 <= n && n < size);
    }
}

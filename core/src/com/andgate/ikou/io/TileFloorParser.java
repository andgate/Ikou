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

package com.andgate.ikou.io;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.MasterSector;
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileSector;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.utility.Array2d;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.utils.Array;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class TileFloorParser
{
    public static Floor parse(String mapString)
    {
        String[] mapLines = mapString.split("\n");

        Vector3i startPosition = getStartPosition(mapString);
        char[][] tileCodes = getTileCodes(mapLines);
        MasterSector masterSector = buildMasterSector(tileCodes);
        Vector3i endPosition = getEndPosition(masterSector);

        TilePalette palette = new TilePalette();

        Floor floor = new Floor(masterSector, palette, startPosition, endPosition);
        return floor;
    }

    private static Vector3i getStartPosition(String firstLine)
    {
        Scanner in = new Scanner(firstLine);

        int startX = Integer.parseInt(in.next());
        int startY = 0;
        int startZ = Integer.parseInt(in.next());

        in.close();

        return new Vector3i(startX, startY, startZ);
    }

    private static Vector3i getEndPosition(MasterSector masterSector)
    {
        Vector3i endPosition = null;

        Array2d<TileSector> sectors = masterSector.getSectors();
        for(int sectorsRowIndex = 0; sectorsRowIndex < sectors.size; sectorsRowIndex++)
        {
            Array<TileSector> sectorsRow = sectors.get(sectorsRowIndex);
            for(int sectorsColumnIndex = 0; sectorsColumnIndex < sectorsRow.size; sectorsColumnIndex++)
            {
                TileSector sector = sectorsRow.get(sectorsColumnIndex);
                if(sector != null)
                {
                    TileStack[][] stacks = sector.getStacks();
                    for (int stacksRowIndex = 0; stacksRowIndex < TileSector.SIZE; stacksRowIndex++)
                    {
                        TileStack[] stacksRow = stacks[stacksRowIndex];
                        for (int stacksColumnIndex = 0; stacksColumnIndex < TileSector.SIZE; stacksColumnIndex++)
                        {
                            // No need for yet another for loop, End tiles should ALWAYS be on the bottom layer.
                            int x = sectorsColumnIndex * TileSector.SIZE + stacksColumnIndex;
                            int y = 0;
                            int z = sectorsRowIndex * TileSector.SIZE + stacksRowIndex;

                            Tile currTile = stacksRow[stacksColumnIndex].get(y);

                            if (currTile == Tile.End)
                            {
                                if (endPosition != null)
                                {
                                    throw new RuntimeException("Multiple end tiles found.");
                                }

                                endPosition = new Vector3i(x, y, z);
                            }
                        }
                    }
                }
            }
        }

        if(endPosition == null)
        {
            throw new RuntimeException("Missing end tile.");
        }

        return endPosition;
    }

    private static char[][] getTileCodes(String[] mapLines)
    {
        int rows = mapLines.length - 1;
        char[][] tiles = new char[rows][];

        // Skip the first line
        for(int i = 1; i < mapLines.length; i++)
        {
            tiles[i-1] = mapLines[i].toCharArray();
        }

        return tiles;
    }

    private static MasterSector buildMasterSector(char[][] tileCodes)
    {
        MasterSector masterSector = new MasterSector();

        for(int row = 0; row < tileCodes.length; row++)
        {
            for(int column = 0; column < tileCodes[row].length; column++)
            {
                char tileCode = tileCodes[row][column];
                TileStack tileStack = new TileStack(tileCode);
                masterSector.setStack(tileStack, row, column);
            }
        }

        return masterSector;
    }

    /*private static String reverseLineOrder(String original)
    {
        String reversed = "";
        Scanner lines = new Scanner(original);
        try
        {
            while (lines.hasNextLine())
            {
                reversed = lines.nextLine() + "\n" + reversed;
            }
        }
        finally
        {
            lines.close();
        }

        return reversed;
    }*/
}

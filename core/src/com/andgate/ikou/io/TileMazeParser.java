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
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileSector;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.model.tile.TileCode;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.model.tile.TileData.TileType;
import com.andgate.ikou.model.tile.TileFactory;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.utils.Array;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class TileMazeParser
{
    public static Floor parse(String mapString)
        throws InvalidFileFormatException
    {
        String[] mapLines = mapString.split("\n");

        Vector3i startPosition = getStartPosition(mapString);
        char[][] tileCodes = getTileCodes(mapLines);
        TileSector masterSector = buildMasterSector(tileCodes);
        Vector3i endPosition = getEndPosition(masterSector);

        Floor floor = new Floor(masterSector, startPosition, endPosition);
        return floor;
    }

    private static Vector3i getStartPosition(String firstLine)
            throws InvalidFileFormatException
    {
        int startX = 0;
        int startY = 0;
        int startZ = 0;

        Scanner in = new Scanner(firstLine);

        try
        {
            try
            {
                startX = Integer.parseInt(in.next());
                startZ = Integer.parseInt(in.next());
            }
            finally
            {
                in.close();
            }
        }
        catch(NumberFormatException  e)
        {
            throw new InvalidFileFormatException("Error reading starting position.");
        }
        catch(NoSuchElementException e)
        {
            throw new InvalidFileFormatException("Error parsing starting position.");
        }

        return new Vector3i(startX, startY, startZ);
    }

    private static Vector3i getEndPosition(TileSector masterSector)
            throws InvalidFileFormatException
    {
        Vector3i endPosition = null;
        for(int z = 0; z < masterSector.size; z++)
        {
            Array<TileStack> sectorRow = masterSector.get(z);
            for(int x = 0; x < sectorRow.size; x++)
            {
                TileStack currTileStack = sectorRow.get(x);
                for(int y = 0; y < currTileStack.size; y++)
                {
                    TileData currTileData = currTileStack.get(y);

                    if(currTileData.getType() == TileType.End)
                    {
                        if(endPosition != null)
                        {
                            throw new InvalidFileFormatException("Multiple end tiles found.");
                        }

                        endPosition = new Vector3i(x, y, z);
                    }
                }
            }
        }

        if(endPosition == null)
        {
            throw new InvalidFileFormatException("Missing end tile.");
        }

        return endPosition;
    }

    private static char[][] getTileCodes(String[] mapLines)
            throws InvalidFileFormatException
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

    private static TileSector buildMasterSector(char[][] tileCodes)
    {
        TileSector masterSector = new TileSector();
        TilePalette palette = new TilePalette();

        for(int row = 0; row < tileCodes.length; row++)
        {
            for(int column = 0; column < tileCodes[row].length; column++)
            {
                char currTileCode = tileCodes[row][column];
                TileStack currTileStack = buildTileStack(currTileCode, palette);
                masterSector.addToRow(currTileStack);
            }

            masterSector.addRow();
        }

        return masterSector;
    }

    private static TileStack buildTileStack(char tileCode, TilePalette palette)
    {
        TileStack tileStack = new TileStack();

        switch(tileCode)
        {
            case TileCode.SMOOTH_TILE:
                tileStack.add(TileFactory.build(TileType.Smooth, palette));
                break;
            case TileCode.OBSTACLE_TILE:
                tileStack.add(TileFactory.build(TileType.Smooth, palette));
                tileStack.add(TileFactory.build(TileType.Obstacle, palette));
                break;
            case TileCode.ROUGH_TILE:
                tileStack.add(TileFactory.build(TileType.Rough, palette));
                break;
            case TileCode.END_TILE:
                tileStack.add(TileFactory.build(TileType.End, palette));
                break;
            default:
                tileStack.add(TileFactory.build(TileType.Blank, palette));
                break;
        }

        return tileStack;
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

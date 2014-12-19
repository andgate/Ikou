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

package com.andgate.ikou.Utility;

import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.TileCode;
import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.math.Vector2;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class TileMazeParser
{
    public static TileMaze parse(String mapString)
        throws InvalidFileFormatException
    {
        String[] mapLines = mapString.split("\n");

        Vector2 startPosition = getStartPosition(mapString);
        char[][] tiles = getTiles(mapLines);

        Vector2 endPosition = getEndPosition(tiles);

        TileMaze maze = new TileMaze(tiles, startPosition, endPosition);
        return maze;
    }

    private static Vector2 getStartPosition(String firstLine)
            throws InvalidFileFormatException
    {
        int startX = 0;
        int startY = 0;

        Scanner in = new Scanner(firstLine);

        try
        {
            try
            {
                startX = Integer.parseInt(in.next());
                startY = Integer.parseInt(in.next());
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

        return new Vector2((float)startX, (float)startY);
    }

    private static Vector2 getEndPosition(char[][] tiles)
            throws InvalidFileFormatException
    {
        Vector2 endPosition = null;

        for(int rowIndex = 0; rowIndex < tiles.length; rowIndex++)
        {
            char[] tileRow = tiles[rowIndex];
            for(int columnIndex = 0; columnIndex < tileRow.length; columnIndex++)
            {
                char currTile = tileRow[columnIndex];
                if(currTile == TileCode.END_TILE)
                {
                    if(endPosition != null)
                    {
                        throw new InvalidFileFormatException("Multiple end tiles found.");
                    }

                    endPosition = new Vector2(columnIndex, rowIndex);
                }
            }
        }

        if(endPosition == null)
        {
            throw new InvalidFileFormatException("Missing end tile.");
        }

        return endPosition;
    }

    private static char[][] getTiles(String[] mapLines)
            throws InvalidFileFormatException
    {
        System.out.println(mapLines);
        int rows = mapLines.length - 1;
        char[][] tiles = new char[rows][];

        // Skip the first line
        for(int i = 1; i < mapLines.length; i++)
        {
            tiles[i-1] = mapLines[i].toCharArray();
        }

        return tiles;
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

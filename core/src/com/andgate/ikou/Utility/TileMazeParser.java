package com.andgate.ikou.Utility;

import com.andgate.ikou.Model.TileMaze;
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

        TileMaze maze = new TileMaze(tiles, startPosition);
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

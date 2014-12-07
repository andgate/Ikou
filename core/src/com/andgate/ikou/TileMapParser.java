package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.andgate.ikou.tiles.EndTileBehavior;
import com.andgate.ikou.tiles.ObstacleTileBehavior;
import com.andgate.ikou.tiles.RoughTileBehavior;
import com.andgate.ikou.tiles.SmoothTileBehavior;
import com.andgate.ikou.tiles.TileBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TileMapParser
{
    public static TileMap parse(String mapString, btDynamicsWorld dynamicsWorld)
        throws InvalidFileFormatException
    {
        Scanner in = new Scanner(mapString);

        try
        {
            Vector2 startPosition = getStartPosition(in);
            ArrayList<Tile> tiles = getTiles(in);

            TileMap map = new TileMap(tiles, startPosition, dynamicsWorld);
            return map;
        }
        finally
        {
            in.close();
        }
    }

    private static Vector2 getStartPosition(Scanner in)
            throws InvalidFileFormatException
    {
        int startX = 0;
        int startY = 0;

        try
        {
            startX = Integer.parseInt(in.next());
            startY = Integer.parseInt(in.next());
            in.nextLine();
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

    private static ArrayList<Tile> getTiles(Scanner in)
            throws InvalidFileFormatException
    {
        ArrayList<Tile> tiles = new ArrayList<Tile>();

        int row = 0;
        int column = 0;

        in.useDelimiter("\\Z");
        String mapDesc = reverseLineOrder(in.next());
        for(int i = 0; i < mapDesc.length(); i++)
        {
            char token = mapDesc.charAt(i);

            TileBehavior behavior = null;

            switch(token)
            {
                case TileCode.SMOOTH_TILE:
                    behavior = new SmoothTileBehavior();
                    break;
                case TileCode.ROUGH_TILE:
                    behavior = new RoughTileBehavior();
                    break;
                case TileCode.OBSTACLE_TILE:
                    behavior = new ObstacleTileBehavior();
                    break;
                case TileCode.END_TILE:
                    behavior = new EndTileBehavior();
                    break;
                default:
                    break;
            }

            if(token == '\n')
            {
                row++;
                column = 0;
            }
            else if(behavior == null)
            {
                throw new InvalidFileFormatException("Unrecognized tile code: " + token);
            }
            else
            {
                tiles.add(new Tile(column, row, behavior));
                column++;
            }
        }

        return tiles;
    }

    private static String reverseLineOrder(String original)
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
    }
}

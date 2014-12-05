package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.andgate.ikou.tiles.EndTileBehavior;
import com.andgate.ikou.tiles.SmoothTileBehavior;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TileMapParser
{
    public static TileMap parse(String mapString, World world)
        throws InvalidFileFormatException
    {
        Scanner in = new Scanner(mapString);

        Vector2 startPosition = getStartPosition(in);
        ArrayList<Tile> tiles = getTiles(in);

        TileMap map = new TileMap(tiles, world, startPosition);

        return map;
    }

    private static Vector2 getStartPosition(Scanner in)
            throws InvalidFileFormatException
    {
        //in.useDelimiter(" ");

        int startX = 0;
        int startY = 0;

        try
        {
            startX = Integer.parseInt(in.next());
            startY = Integer.parseInt(in.next());
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

        in.useDelimiter("");
        while(in.hasNext())
        {
            String token = in.next();

            TileBehavior behavior = null;

            switch(token)
            {
                case TileCode.SMOOTH_TILE:
                    behavior = new SmoothTileBehavior();
                    break;
                case TileCode.END_TILE:
                    behavior = new EndTileBehavior();
                    break;
                default:
                    break;
            }

            if(token.equals("\n"))
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

}

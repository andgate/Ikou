package com.andgate.ikou.Tiles;

import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Utility.Array2d;

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
}

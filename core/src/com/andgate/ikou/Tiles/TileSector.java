package com.andgate.ikou.Tiles;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Render.TileMeshBuilder;
import com.andgate.ikou.Utility.Array2d;
import com.badlogic.gdx.graphics.Mesh;
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
}

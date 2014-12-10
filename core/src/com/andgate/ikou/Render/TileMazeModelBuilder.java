package com.andgate.ikou.Render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Tiles.TileSector;
import com.andgate.ikou.Tiles.TileStack;
import com.andgate.ikou.Tiles.TileData;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class TileMazeModelBuilder
{
    public static Model build(TileMaze maze)
    {
        // Build a master sector of the maze
        TileSector masterSector = new TileSector(maze);

        System.out.println(masterSector.toString());

        // Build the model with the tile data
        int tileAttributes = Usage.Position | Usage.Normal;

        ModelBuilder mob = new ModelBuilder();
        MeshPartBuilder mpb;
        mob.begin();
        for(int z = 0; z < masterSector.size; z++)
        {
            Array<TileStack> currSectorRow = masterSector.get(z);

            for(int x = 0; x < currSectorRow.size; x++)
            {
                TileStack currTileStack = currSectorRow.get(x);

                for(int y = 0; y < currTileStack.size; y++)
                {
                    TileData currTile = currTileStack.get(y);

                    if(currTile.isVisible())
                    {
                        float width = Constants.TILE_LENGTH;
                        float height = Constants.TILE_THICKNESS;
                        float depth = Constants.TILE_LENGTH;

                        float xPos = (float) x * width;
                        float yPos = (float) y * height;
                        float zPos = (float) z * depth;

                        String name = "Tile" + ((x + 1) * (y + 1) * (z + 1));
                        Material tileMaterial = currTile.getMaterial();

                        mob.node().id = name;
                        mob.node().translation.set(xPos, yPos, zPos);
                        mpb = mob.part(name, GL20.GL_TRIANGLES, tileAttributes, tileMaterial);
                        mpb.box(0.0f, 0.0f, 0.0f, width, height, depth);
                    }
                }
            }
        }

        return mob.end();
    }
}

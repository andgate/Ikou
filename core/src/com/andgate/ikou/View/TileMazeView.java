package com.andgate.ikou.View;

import com.andgate.ikou.DirectionListener;
import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.TileCode;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class TileMazeView implements Disposable
{
    TileMaze maze;

    private Tile[] tiles;

    public TileMazeView(TileMaze maze)
    {
        this.maze = maze;
        buildTiles();
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        for(Tile tile : tiles)
        {
            tile.render(modelBatch, environment);
        }
    }

    @Override
    public void dispose()
    {
    }

    private void buildTiles()
    {
        int tileCount = maze.getTileCount();
        tiles = new Tile[tileCount];

        int currentTileIndex = 0;
        float y = 0.0f;
        for(int z = 0; z < maze.getTiles().length; z++)
        {
            for(int x = 0; x < maze.getTiles()[z].length; x++)
            {
                switch(maze.getTiles()[z][x])
                {
                    case TileCode.SMOOTH_TILE:
                        tiles[currentTileIndex] = new Tile(new Vector3(x, y, z));
                        break;
                    case TileCode.OBSTACLE_TILE:
                        tiles[currentTileIndex] = new Obstacle(new Vector3(x, y, z));
                    default:
                        break;
                }

                if(maze.getTiles()[z][x] != ' ')
                    currentTileIndex++;
            }
        }
    }

    /*private void buildModel()
    {
        int tileAttributes = Usage.Position | Usage.Normal;
        Material tileMaterial = new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY));

        ModelBuilder mob = new ModelBuilder();
        MeshPartBuilder mpb;
        mob.begin();
        char[][] tiles = maze.getTiles();
        for(int y = 0; y < tiles.length; y++)
        {
            for(int x = 0; x < tiles[y].length; x++)
            {
                float xPos = (float)x;
                float yPos = 0.0f;
                float zPos = (float)y;
                float width = Constants.TILE_LENGTH;
                float height = Constants.TILE_THICKNESS;
                float depth = Constants.TILE_LENGTH;
                String name = "Tile" + ((x+1) * (y+1));

                mob.node().id = name;
                mpb = mob.part(name, GL20.GL_TRIANGLES, tileAttributes, tileMaterial);
                mpb.box(xPos, yPos, zPos, width, height, depth);
            }
        }

        mazeModel = mob.end();
        mazeModelInstance = new ModelInstance(mazeModel);
    }*/
}

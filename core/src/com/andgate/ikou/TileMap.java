package com.andgate.ikou;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;

import java.util.ArrayList;

public class TileMap
{
    private int width;
    private int height;
    private Vector2 startPosition;

    private ArrayList<Tile> tiles;

    private ShapeRenderer shapeRenderer;

    public TileMap(ArrayList<Tile> tiles, Vector2 startPosition, btDynamicsWorld dynamicsWorld)
    {
        this.tiles = tiles;
        this.startPosition = startPosition;
        shapeRenderer = new ShapeRenderer();

        Tile lastTile = tiles.get(tiles.size() - 1);
        width = lastTile.getX() + 1;
        height = lastTile.getY() + 1;

        createMapBounds();
        createTiles(dynamicsWorld);
    }

    private void createTiles(btDynamicsWorld dynamicsWorld)
    {
        for(Tile tile : tiles)
        {
            tile.create(dynamicsWorld);
        }
    }

    private void createMapBounds()
    {
    }

    public void update()
    {
        for(Tile tile : tiles)
        {
            tile.update();
        }
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        for(Tile tile : tiles)
        {
            tile.render(modelBatch, environment);
        }
    }

    public Vector2 getStartPosition()
    {
        return startPosition;
    }

    public void dispose()
    {
        for(Tile tile : tiles)
        {
            tile.dispose();
        }
    }
}

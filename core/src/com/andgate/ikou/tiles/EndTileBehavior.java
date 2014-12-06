package com.andgate.ikou.tiles;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Tile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class EndTileBehavior implements TileBehavior
{
    @Override
    public void create(World world, Tile tile)
    {}

    @Override
    public void update()
    {

    }

    @Override
    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0.0f, 0.0f, Constants.TILE_LENGTH, Constants.TILE_LENGTH);
    }
}

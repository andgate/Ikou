package com.andgate.ikou.tiles;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Tile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EndTileBehavior implements TileBehavior
{
    @Override
    public void create(Tile tile)
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

    @Override
    public void dispose()
    {

    }
}

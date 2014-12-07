package com.andgate.ikou.tiles;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Tile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ObstacleTileBehavior implements TileBehavior
{
    @Override
    public void create( Tile tile)
    {
    }

    @Override
    public void update()
    {

    }

    @Override
    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(Color.DARK_GRAY);

        // Points on a box
        final float thickness = Constants.MEDIUM_THICKNESS;
        final float width = Constants.TILE_LENGTH;
        final float height = Constants.TILE_LENGTH;
        final float x = 0.0f;
        final float y = 0.0f;

        // Create vertices for a box
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(x+thickness, y+thickness, width-(thickness*2.0f), height-(thickness*2.0f));
    }

    @Override
    public void dispose()
    {

    }
}

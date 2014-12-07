package com.andgate.ikou.tiles;

import com.andgate.ikou.Tile;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface TileBehavior
{
    public void create(Tile tile);
    public void update();
    public void render(ShapeRenderer shapeRenderer);
    public void dispose();
}

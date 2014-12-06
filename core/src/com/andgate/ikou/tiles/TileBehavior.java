package com.andgate.ikou.tiles;

import com.andgate.ikou.Tile;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

public interface TileBehavior
{
    public void create(World world, Tile tile);
    public void update();
    public void render(ShapeRenderer shapeRenderer);
}

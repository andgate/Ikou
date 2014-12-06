package com.andgate.ikou;

import com.andgate.ikou.tiles.TileBehavior;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Tile
{
    public TileBehavior behavior;
    private int x;
    private int y;

    public Tile(int x, int y, TileBehavior behavior)
    {
        this.x = x;
        this.y = y;
        this.behavior = behavior;
    }

    public void create(World world)
    {
        behavior.create(world, this);
    }

    public void update()
    {
        behavior.update();
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.identity();
        shapeRenderer.translate((float)x, (float)y, 0.0f);
        behavior.render(shapeRenderer);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}

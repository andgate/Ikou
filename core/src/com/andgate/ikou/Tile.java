package com.andgate.ikou;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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

    public void update()
    {
        behavior.update();
    }

    public void render(ShapeRenderer shapeRenderer)
    {
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

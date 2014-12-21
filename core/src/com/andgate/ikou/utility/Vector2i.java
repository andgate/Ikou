package com.andgate.ikou.utility;

import com.badlogic.gdx.math.Vector2;

public class Vector2i
{
    public int x;
    public int y;

    public Vector2i()
    {
        this(0, 0);
    }

    public Vector2i(int x, int y)
    {
        set(x, y);
    }

    public Vector2i(Vector2i other)
    {
        set(other);
    }

    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2i other)
    {
        set(other.x, other.y);
    }

    public void set(Vector2 other)
    {
        set(Math.round(other.x), Math.round(other.y));
    }

    public void add(int x, int y)
    {
        this.x += x;
        this.y += y;
    }

    public void add(Vector2i other)
    {
        add(other.x, other.y);
    }

    public void sub(int x, int y)
    {
        this.x -= x;
        this.y -= y;
    }

    public void sub(Vector2i other)
    {
        sub(other.x, other.y);
    }

    public boolean equals(int x, int y)
    {
        return ( (this.x == x) && (this.y == y) );
    }

    public boolean equals(Vector2i other)
    {
        return equals(other.x, other.y);
    }

    public float len()
    {
        return (float)Math.sqrt(x * x + y * y);
    }
}

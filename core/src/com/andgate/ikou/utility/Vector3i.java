package com.andgate.ikou.utility;

import com.badlogic.gdx.math.Vector3;

public class Vector3i
{
    public int x;
    public int y;
    public int z;

    public Vector3i()
    {
        this(0, 0, 0);
    }

    public Vector3i(int x, int y, int z)
    {
        set(x, y, z);
    }

    public Vector3i(Vector3i other)
    {
        set(other);
    }

    public void set(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3i other)
    {
        set(other.x, other.y, other.z);
    }

    public void set(float x, float y, float z)
    {
        set(Math.round(x), Math.round(y), Math.round(z));
    }

    public void set(Vector3 other)
    {
        set(other.x, other.y, other.z);
    }

    public void add(int x, int y, int z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add(Vector3i other)
    {
        add(other.x, other.y, other.z);
    }

    public void sub(int x, int y, int z)
    {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public void sub(Vector3i other)
    {
        sub(other.x, other.y, other.z);
    }

    public boolean equals(int x, int y, int z)
    {
        return ( (this.x == x) && (this.y == y) && (this.z == z) );
    }

    public boolean equals(Vector3i other)
    {
        return equals(other.x, other.y, other.z);
    }
}
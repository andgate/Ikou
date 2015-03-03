/*
    This file is part of Ikou.
    Ikou is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License.
    Ikou is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with Ikou.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        return (float)Math.sqrt((float)x * x + (float)y * y);
    }
}

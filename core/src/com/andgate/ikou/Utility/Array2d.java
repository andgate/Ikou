package com.andgate.ikou.utility;

import com.badlogic.gdx.utils.Array;

public class Array2d<T> extends Array<Array<T>>
{
    public Array2d()
    {
        super();
        add(new Array<T>());
    }

    public T get(int i, int j)
    {
        return get(i).get(j);
    }

    public void set(int i, int j, T value)
    {
        get(i).set(j, value);
    }

    public void addToRow(T value)
    {
        get(size - 1).add(value);
    }

    public void addRow()
    {
        add(new Array<T>());
    }

    public int countRows()
    {
        return size;
    }

    public int maxColumns()
    {
        int max = 0;
        for(int row = 0; row < size; row++)
        {
            int columnCount = get(row).size;
            if(columnCount > max)
            {
                max = columnCount;
            }
        }

        return max;
    }

    public T last()
    {
        int lastRowIndex = size - 1;
        int lastColumnIndex = get(lastRowIndex).size - 1;
        return get(lastRowIndex).get(lastColumnIndex);
    }
}

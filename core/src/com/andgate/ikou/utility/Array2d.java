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

import com.badlogic.gdx.utils.Array;

public class Array2d<T> extends Array<Array<T>>
{
    public Array2d()
    {
        super();
        add(new Array<T>());
    }

    public T get(int rowIndex, int columnIndex)
    {
        if(rowIndex < size)
        {
            Array<T> row = get(rowIndex);
            if(columnIndex < row.size)
            {
                return row.get(columnIndex);
            }
        }

        return null;
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

    @Override
    public Array<T>[] shrink()
    {
        for(Array<T> item : items)
        {
            item.shrink();
        }

        return super.shrink();
    }
}

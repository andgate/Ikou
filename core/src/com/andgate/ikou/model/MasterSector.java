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

package com.andgate.ikou.model;

import com.andgate.ikou.utility.Array2d;
import com.andgate.ikou.model.TileStack.Tile;
import com.badlogic.gdx.utils.Array;

public class MasterSector
{
    private static final String TAG = "MasterSector";
    private Array2d<TileSector> sectors;

    public MasterSector()
    {
        sectors = new Array2d<>();
    }

    public void setStack(TileStack stack, int tileRow, int tileColumn)
    {
        TileSector sector = getSectorFromTileCoords(tileRow, tileColumn);

        if(sector == null)
        {
            sector = ensuredGetSector(toSectorIndex(tileRow), toSectorIndex(tileColumn));
        }

        // put the given tile into the given sector
        int rowInSector = coordInSector(tileRow);
        int columnInSector = coordInSector(tileColumn);
        sector.set(stack, rowInSector, columnInSector);
    }

    public Tile get(int x, int y, int z)
    {
        TileSector sector = getSectorFromTileCoords(z, x);

        if(sector != null)
        {
            int rowInSector = coordInSector(z);
            int columnInSector = coordInSector(x);

            Tile tile = sector.getTile(columnInSector, y, rowInSector);
            if(tile != null)
            {
                return tile;
            }
        }

        return Tile.Blank;
    }

    public TileStack getTileStack(int tileRow, int tileColumn)
    {
        TileSector sector = getSectorFromTileCoords(tileRow, tileColumn);

        if(sector != null)
        {
            int rowInSector = coordInSector(tileRow);
            int columnInSector = coordInSector(tileColumn);

            TileStack stack = sector.getStacks()[rowInSector][columnInSector];
            if(stack != null)
            {
                return stack;
            }
        }

        return null;
    }

    public Array2d<TileSector> getSectors()
    {
        return sectors;
    }

    public TileSector getSectorFromTileCoords(int tileRow, int tileColumn)
    {
        int sectorRowIndex = toSectorIndex(tileRow);
        int sectorColumnIndex = toSectorIndex(tileColumn);

        return sectors.get(sectorRowIndex, sectorColumnIndex);
    }

    public int getHeight()
    {
        return sectors.size * TileSector.SIZE;
    }

    public int getWidth(int sectorsRowIndex)
    {
        return sectors.get(sectorsRowIndex).size * TileSector.SIZE;
    }

    public int toSectorIndex(int tileCoord)
    {
        return tileCoord / TileSector.SIZE;
    }

    public int coordInSector(int coord)
    {
        return coord % TileSector.SIZE;
    }

    public TileSector ensuredGetSector(int rowIndex, int columnIndex)
    {
        Array<TileSector> sectorsRow = ensureSectorRow(rowIndex);
        return ensureSectorColumn(sectorsRow, columnIndex);
    }

    private Array<TileSector> ensureSectorRow(int rowIndex)
    {
        int rowCount = rowIndex + 1;
        if(rowCount > sectors.size)
        {
            int distance = rowCount - sectors.size;
            sectors.ensureCapacity(distance);

            while(distance > 0)
            {
                sectors.add(null);
                distance--;
            }
        }

        Array<TileSector> sectorsRow = sectors.get(rowIndex);
        if(sectorsRow == null)
        {
            sectorsRow = new Array<>();
            sectors.set(rowIndex, sectorsRow);
        }

        return sectorsRow;
    }

    private TileSector ensureSectorColumn(Array<TileSector> sectorsRow, int columnIndex)
    {
        int columnCount = columnIndex +1;
        if(columnCount > sectorsRow.size)
        {
            int distance = columnCount - sectorsRow.size;
            sectorsRow.ensureCapacity(distance);

            while(distance > 0)
            {
                sectorsRow.add(null);
                distance--;
            }
        }

        TileSector sector = sectorsRow.get(columnIndex);
        if(sector == null)
        {
            sector = new TileSector();
            sectorsRow.set(columnIndex, sector);
        }

        return sector;
    }

    public void shrink()
    {
        //sectors.shrink();

        for(Array<TileSector> sectorsRow : sectors)
        {
            sectorsRow.shrink();
        }
    }
}
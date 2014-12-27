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

import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;

public class Floor
{
    private TileSector masterSector;
    private Vector3i start;
    private Vector3i end;
    private Vector2i offset = new Vector2i();

    public Floor(TileSector masterSector, Vector3i start, Vector3i end)
    {
        this.masterSector = masterSector;
        this.start = start;
        this.end = end;
    }

    public TileSector getMasterSector()
    {
        return masterSector;
    }

    public Vector3i getStart()
    {
        return start;
    }

    public Vector3i getEnd()
    {
        return end;
    }

    public Vector2i getOffset()
    {
        return offset;
    }
}

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

package com.andgate.ikou.model.tile;

import com.andgate.ikou.model.TileStack;

public class TileData
{

    private TileStack.Tile type;

    public TileData()
    {
        this(TileStack.Tile.Blank);
    }

    public TileData(TileStack.Tile type)
    {
        this.type = type;
    }

    public void setType(TileStack.Tile type)
    {
        this.type = type;
    }

    public TileStack.Tile getType()
    {
        return type;
    }

}

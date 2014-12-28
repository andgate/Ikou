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

import com.andgate.ikou.Constants;

public class LevelData
{
    public final String name;
    public final int totalFloors;
    public int completedFloors;

    public final String LEVEL_DIRECTORY;

    public LevelData(String name, int totalFloors, int completedFloors)
    {
        this.name = name;
        this.totalFloors = totalFloors;
        this.completedFloors = completedFloors;

        LEVEL_DIRECTORY = Constants.LEVELS_INTERNAL_PATH + "old/" + name + "/";
    }

    public String getFloorPath(int floor)
    {
        String path
                = LEVEL_DIRECTORY + floor + Constants.FLOOR_EXTENSION;
        return path;
    }
}

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

package com.andgate.ikou.io;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelLoader
{
    public static Level load(LevelData levelData)
        throws InvalidFileFormatException
    {
        Floor[] floors = new Floor[levelData.totalFloors];

        for(int floorIndex = 0; floorIndex < floors.length; floorIndex++)
        {
            String floorFilePath = levelData.getFloorPath(floorIndex + 1);
            FileHandle floorFile = Gdx.files.internal(floorFilePath);
            floors[floorIndex] = (TileFloorParser.parse(floorFile.readString()));
        }

        return new Level(floors, levelData.name);
    }
}

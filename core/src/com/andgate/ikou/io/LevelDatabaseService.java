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

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.model.ProgressDatabase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class LevelDatabaseService
{
    public static Level[] getLevels()
    {
        FileHandle[] levelFiles = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH).list();

        Array<Level> levels = new Array<>();

        for(FileHandle levelFile : levelFiles)
        {
            String extension = levelFile.extension();

            if(!levelFile.isDirectory() && extension.equals(Constants.LEVEL_EXTENSION_NO_DOT))
            {
                levels.add(LevelService.read(levelFile));
            }
        }

        return levels.toArray(new Level[levels.size].getClass().getComponentType());
    }

    public static LevelData[] getOldLevels()
    {
        ProgressDatabase progressDB = ProgressDatabaseService.read();
        FileHandle[] levelsDirFolders = Gdx.files.internal(Constants.LEVELS_INTERNAL_PATH + "old/").list();

        LevelData[] levels = new LevelData[levelsDirFolders.length];

        for(int i = 0; i < levelsDirFolders.length; i++)
        {
            FileHandle level = levelsDirFolders[i];

            if(level.isDirectory())
            {
                String levelName = level.name();
                int totalFloors = level.list().length;
                int completedFloors = progressDB.getFloorsCompleted(levelName);

                levels[i] = new LevelData(levelName, totalFloors, completedFloors);
            }
        }

        return levels;
    }
}

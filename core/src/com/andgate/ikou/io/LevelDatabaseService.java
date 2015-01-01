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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class LevelDatabaseService
{
    private static final String TAG = "LevelDatabaseService";
    public static Level[] getLevels()
    {
        FileHandle[] levelFiles = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH).list();

        Array<Level> levels = new Array<>();

        for(FileHandle levelFile : levelFiles)
        {
            String extension = levelFile.extension();

            if(!levelFile.isDirectory() && extension.equals(Constants.LEVEL_EXTENSION_NO_DOT))
            {
                try
                {
                    levels.add(LevelService.read(levelFile));
                }
                catch(final IOException e)
                {
                    final String errorMessage = "Failed to read level file.";
                    Gdx.app.error(TAG, errorMessage, e);
                }
            }
        }

        return levels.toArray(new Level[levels.size].getClass().getComponentType());
    }

    public static LevelData[] getLevelDatas()
    {
        ProgressDatabase progressDB = ProgressDatabaseService.read();

        LevelData[] levelDatasInternal = getLevelDatasInternal(progressDB);
        LevelData[] levelDatasExternal = getLevelDatasExternal(progressDB);

        LevelData[] levelDatas = new LevelData[levelDatasInternal.length + levelDatasExternal.length];
        System.arraycopy(levelDatasInternal, 0, levelDatas, 0, levelDatasInternal.length);
        System.arraycopy(levelDatasExternal, 0, levelDatas, levelDatasInternal.length, levelDatasExternal.length);

        return levelDatas;
    }

    public static LevelData[] getLevelDatasInternal(ProgressDatabase progressDB)
    {
        FileHandle[] levelsFolder = Gdx.files.internal(Constants.LEVELS_INTERNAL_PATH).list();
        return readLevelDatas(levelsFolder, progressDB);
    }

    public static LevelData[] getLevelDatasExternal(ProgressDatabase progressDB)
    {
        FileHandle[] levelsFolder = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH).list();
        return readLevelDatas(levelsFolder, progressDB);
    }

    public static LevelData[] readLevelDatas(FileHandle[] levelFolder, ProgressDatabase progressDB)
    {
        Array<LevelData> levelDatas = new Array<>();

        for(int i = 0; i < levelFolder.length; i++)
        {
            FileHandle levelFile = levelFolder[i];

            if(levelFile.exists() && levelFile.extension().equals(Constants.LEVEL_EXTENSION_NO_DOT))
            {
                try
                {
                    levelDatas.add(getLevelData(levelFile, progressDB));
                }
                catch(final IOException e)
                {
                    final String errorMessage = "Error reading level data.";
                    Gdx.app.error(TAG, errorMessage, e);
                }
            }
        }

        return levelDatas.toArray(new LevelData[levelDatas.size].getClass().getComponentType());
    }

    public static LevelData getLevelData(final FileHandle levelFile, ProgressDatabase progressDB)
            throws IOException
    {
        final String levelName = levelFile.nameWithoutExtension();
        final int completedFloors = progressDB.getFloorsCompleted(levelName);

        final InputStream levelIn = new GZIPInputStream(levelFile.read());
        final int totalFloors = levelIn.read();
        levelIn.close();

        return new LevelData(levelName, totalFloors, completedFloors);
    }

    public static LevelData[] getOldLevelDatas()
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

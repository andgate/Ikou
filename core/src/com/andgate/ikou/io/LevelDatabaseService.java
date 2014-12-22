package com.andgate.ikou.io;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.model.ProgressDatabase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class LevelDatabaseService
{
    public static LevelData[] getLevels()
    {
        ProgressDatabase progressDB = ProgressDatabaseService.read();
        FileHandle[] levelsDirFolders = Gdx.files.internal(Constants.LEVELS_DIRECTORY).list();

        LevelData[] levels = new LevelData[levelsDirFolders.length];

        for(int i = 0; i < levelsDirFolders.length; i++)
        {
            FileHandle level = levelsDirFolders[i];

            if(level.isDirectory())
            {
                String levelName = level.name();
                int totalFloors = levelsDirFolders.length;
                int completedFloors = progressDB.getFloorsCompleted(levelName);

                levels[i] = new LevelData(levelName, totalFloors, completedFloors);
            }
        }

        return levels;

    }

    public static String[] getLevelNames()
    {
        FileHandle dirHandle = Gdx.files.internal(Constants.LEVELS_DIRECTORY);

        ArrayList<String> levelNamesList = new ArrayList<String>();

        for(FileHandle entry : dirHandle.list())
        {
            if(entry.isDirectory())
            {
                String levelName = entry.name();
                levelNamesList.add(levelName);
            }
        }

        return levelNamesList.toArray(new String[levelNamesList.size()]);
    }
}

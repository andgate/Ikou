package com.andgate.ikou;

import com.andgate.ikou.Model.TileMaze;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class LevelDatabaseService
{
    public static LevelData[] getLevels()
    {
        ProgressDatabase progressDB = ProgressDatabaseService.read();
        ArrayList<LevelData> levelsList = new ArrayList<>();

        FileHandle levelsDir = Gdx.files.internal(Constants.LEVELS_DIRECTORY);

        for(FileHandle level : levelsDir.list())
        {
            if(level.isDirectory())
            {
                String levelName = level.name();
                int totalFloors = level.list().length;
                int completedFloors = progressDB.getFloorsVisited(levelName);

                levelsList.add(new LevelData(levelName, totalFloors, completedFloors));
            }
        }

        return levelsList.toArray(new LevelData[levelsList.size()]);

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

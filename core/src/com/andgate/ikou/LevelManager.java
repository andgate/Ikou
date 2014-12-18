package com.andgate.ikou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class LevelManager
{
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

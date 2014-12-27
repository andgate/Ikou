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
            String mazeFilePath = levelData.getFloorPath(floorIndex + 1);
            FileHandle mazeFile = Gdx.files.internal(mazeFilePath);
            floors[floorIndex] = (TileFloorParser.parse(mazeFile.readString()));
        }

        return new Level(floors, levelData.name);
    }
}

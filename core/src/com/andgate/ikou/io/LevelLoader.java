package com.andgate.ikou.io;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.model.TileMazeSimulator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelLoader
{
    public static Level load(LevelData levelData, int startingFloor)
        throws InvalidFileFormatException
    {
        Floor[] floors = new Floor[levelData.totalFloors];

        for(int mazeIndex = 0; mazeIndex < floors.length; mazeIndex++)
        {
            String mazeFilePath = levelData.getFloorPath(mazeIndex + 1);
            FileHandle mazeFile = Gdx.files.internal(mazeFilePath);
            floors[mazeIndex] = TileMazeParser.parse(mazeFile.readString());
        }

        return new Level(levelData, floors, startingFloor);
    }
}

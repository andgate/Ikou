package com.andgate.ikou.io;

import com.andgate.ikou.LevelData;
import com.andgate.ikou.Level;
import com.andgate.ikou.TileMaze;
import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelLoader
{
    public static Level load(LevelData levelData, int startingFloor)
        throws InvalidFileFormatException
    {
        TileMaze[] mazes = new TileMaze[levelData.totalFloors];

        for(int mazeIndex = 0; mazeIndex < mazes.length; mazeIndex++)
        {
            String mazeFilePath = levelData.getFloorPath(mazeIndex + 1);
            FileHandle mazeFile = Gdx.files.internal(mazeFilePath);
            mazes[mazeIndex] = TileMazeParser.parse(mazeFile.readString());
        }

        return new Level(levelData, mazes, startingFloor);
    }
}

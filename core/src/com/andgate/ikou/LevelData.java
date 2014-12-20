package com.andgate.ikou;

import com.andgate.ikou.Constants;

public class LevelData
{
    public final String name;
    public final int totalFloors;
    public final int completedFloors;

    public final String LEVEL_DIRECTORY;

    public LevelData(String name, int totalFloors, int completedFloors)
    {
        this.name = name;
        this.totalFloors = totalFloors;
        this.completedFloors = completedFloors;

        LEVEL_DIRECTORY = Constants.LEVELS_DIRECTORY + "/" + name + "/";
    }

    public String getFloorPath(int floor)
    {
        String path
                = LEVEL_DIRECTORY + floor + Constants.FLOOR_EXTENSION;
        return path;
    }
}

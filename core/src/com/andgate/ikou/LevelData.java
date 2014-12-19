package com.andgate.ikou;

public class LevelData
{
    public final String name;
    public final int totalFloors;
    public final int completedFloors;

    public LevelData(String name, int totalFloors, int completedFloors)
    {
        this.name = name;
        this.totalFloors = totalFloors;
        this.completedFloors = completedFloors;
    }
}

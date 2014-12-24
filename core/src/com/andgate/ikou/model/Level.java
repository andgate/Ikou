package com.andgate.ikou.model;

import com.andgate.ikou.Constants;
import com.andgate.ikou.io.ProgressDatabaseService;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.math.Vector3;

public class Level implements TileMazeSimulator.WinListener
{
    public final LevelData levelData;
    public Floor[] floors;
    private int startingFloor;
    private int currentFloor;

    public Level(LevelData levelData, Floor[] floors, int startingFloor)
    {
        this.levelData = levelData;
        this.floors = floors;
        this.startingFloor = startingFloor;
        this.currentFloor = startingFloor;
    }

    public Floor[] getFloors()
    {
        return floors;
    }

    public String getName()
    {
        return levelData.name;
    }

    public int getCompletedFloors()
    {
        return levelData.completedFloors;
    }

    public int getTotalFloors()
    {
        return levelData.totalFloors;
    }

    public int getCurrentFloorNumber()
    {
        return currentFloor;
    }

    public Floor getCurrentFloor()
    {
        return floors[currentFloor - 1];
    }

    private Vector3 initialPlayerPosition = new Vector3();
    public Vector3 getIntialPlayerPosition()
    {
        Vector3i initialFloorStart = floors[startingFloor - 1].getStart();
        Vector2i offset = calculateFloorOffset(startingFloor);

        Vector3 initialPlayerPosition = new Vector3();
        initialPlayerPosition.x = initialFloorStart.x + offset.x;
        initialPlayerPosition.y = Constants.TILE_THICKNESS - (startingFloor - 1) * Constants.FLOOR_SPACING;
        initialPlayerPosition.z = initialFloorStart.z + offset.y;

        return initialPlayerPosition;
    }

    public Vector3 getCurrentPlayerPosition()
    {
        Vector3i currentFloorStart = floors[currentFloor - 1].getStart();
        Vector2i offset = calculateFloorOffset(currentFloor);

        Vector3 currentPlayerPosition = new Vector3();
        currentPlayerPosition.x = currentFloorStart.x + offset.x;
        currentPlayerPosition.y = Constants.TILE_THICKNESS - (currentFloor - 1) * Constants.FLOOR_SPACING;
        currentPlayerPosition.z = currentFloorStart.z + offset.y;

        return currentPlayerPosition;
    }

    public float getPlayerY()
    {
        return Constants.TILE_THICKNESS - (currentFloor - 1) * Constants.FLOOR_SPACING;
    }

    public Vector2i calculateFloorOffset(int floor)
    {
        Vector2i offset = new Vector2i(0, 0);

        for(int floorIndex = 1; floorIndex < floor; floorIndex++)
        {
            Vector3i currentStart = floors[floorIndex].getStart();
            Vector3i lastEnd = floors[floorIndex-1].getEnd();

            offset.add(lastEnd.x, lastEnd.z);
            offset.sub(currentStart.x, currentStart.z);
        }

        return offset;
    }

    @Override
    public void mazeWon()
    {
        saveProgress();
        currentFloor++;
    }

    private void saveProgress()
    {
        if(currentFloor > levelData.completedFloors)
        {
            levelData.completedFloors = currentFloor;

            ProgressDatabase progressDB = ProgressDatabaseService.read();
            progressDB.setFloorsCompleted(getName(), currentFloor);

            ProgressDatabaseService.write(progressDB);
        }
    }
}

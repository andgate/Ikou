package com.andgate.ikou.model;

import com.andgate.ikou.Constants;
import com.andgate.ikou.io.ProgressDatabaseService;
import com.andgate.ikou.utility.Vector2i;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Level implements TileMaze.WinListener
{
    public final LevelData levelData;
    public final TileMaze[] mazes;
    private int startingFloor;
    private int currentFloor;

    public Level(LevelData levelData, TileMaze[] mazes, int startingFloor)
    {
        this.levelData = levelData;
        this.mazes = mazes;
        this.startingFloor = startingFloor;
        this.currentFloor = startingFloor;

        for(TileMaze maze : mazes)
        {
            maze.addWinListener(this);
        }
    }

    public TileMaze[] getMazes()
    {
        return mazes;
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

    public TileMaze getCurrentTileMaze()
    {
        return mazes[currentFloor - 1];
    }

    private Vector3 initialPlayerPosition = new Vector3();
    public Vector3 getIntialPlayerPosition()
    {
        Vector2i initialMazePlayerPosition = mazes[startingFloor - 1].getStartPosition();
        Vector2i offset = calculateFloorOffset(startingFloor);

        Vector3 initialPlayerPosition = new Vector3();
        initialPlayerPosition.x = initialMazePlayerPosition.x + offset.x;
        initialPlayerPosition.y = Constants.TILE_THICKNESS - (startingFloor - 1) * Constants.FLOOR_SPACING;
        initialPlayerPosition.z = initialMazePlayerPosition.y + offset.y;

        return initialPlayerPosition;
    }

    public Vector3 getCurrentPlayerPosition()
    {
        Vector2i currentMazePlayerPosition = mazes[currentFloor - 1].getPlayerPosition();
        Vector2i offset = calculateFloorOffset(currentFloor);

        Vector3 currentPlayerPosition = new Vector3();
        currentPlayerPosition.x = currentMazePlayerPosition.x + offset.x;
        currentPlayerPosition.y = Constants.TILE_THICKNESS - (currentFloor - 1) * Constants.FLOOR_SPACING;
        currentPlayerPosition.z = currentMazePlayerPosition.y + offset.y;

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
            TileMaze currMaze = mazes[floorIndex];
            TileMaze lastMaze = mazes[floorIndex-1];

            offset.add(lastMaze.getEndPosition());
            offset.sub(currMaze.getStartPosition());
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

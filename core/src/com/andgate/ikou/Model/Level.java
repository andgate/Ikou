package com.andgate.ikou.Model;

import com.andgate.ikou.Constants;
import com.andgate.ikou.LevelData;
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

        /*for(TileMaze maze : mazes)
        {
            maze.addWinListener(this);
        }*/
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

    Vector3 initialPlayerPosition = new Vector3();

    public Vector3 getIntialPlayerPostion()
    {
        Vector2 initialMazePlayerPosition = mazes[startingFloor - 1].getStartPosition();
        initialPlayerPosition.x = initialMazePlayerPosition.x;
        initialPlayerPosition.y = Constants.TILE_THICKNESS - (startingFloor - 1) * Constants.FLOOR_SPACING;
        initialPlayerPosition.z = initialMazePlayerPosition.y;

        return initialPlayerPosition;
    }

    @Override
    public void mazeWon()
    {
        /*currentFloor++;

        // save progress
        ProgressDatabase progressDB = ProgressDatabaseService.read();
        int farthestFloor = progressDB.getFloorsVisited(getName());

        if(currentFloor > farthestFloor)
        {
            progressDB.setFloorsVisited(getName(), currentFloor);
            ProgressDatabaseService.write(progressDB);
        }*/
    }
}

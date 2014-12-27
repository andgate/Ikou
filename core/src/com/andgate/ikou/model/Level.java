package com.andgate.ikou.model;

import com.andgate.ikou.Constants;
import com.andgate.ikou.io.ProgressDatabaseService;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Level
{
    public Array<Floor> floors = new Array<>();
    private String name;

    public Level()
    {
        name = "";
    }

    public Level(String name)
    {
        this.name = name;
    }

    public Level(Floor[] floors)
    {
        this();
        this.floors.addAll(floors, 0, floors.length);
        calculateFloorOffsets();
    }

    public Level(Floor[] floors, String name)
    {
        this(floors);
        setName(name);
    }

    public Floor[] getFloors()
    {
        return floors.toArray(new Floor[floors.size].getClass().getComponentType());
    }

    public void addFloor(Floor floor)
    {
        floors.add(floor);
    }

    public Floor getFloor(int floorNumber)
    {
        int floorIndex = floorNumber - 1;
        return floors.get(floorIndex);
    }

    private Vector3 startPosition = new Vector3();
    public Vector3 getStartPosition(int floorIndex)
    {
        Vector3i floorStart = floors.get(floorIndex).getStart();
        startPosition.set(floorStart.x, 0.0f, floorStart.z);

        Vector2i offset = getFloorOffset(floorIndex);
        startPosition.add(offset.x, 0.0f, offset.y);

        startPosition.y = TileData.HEIGHT - floorIndex * Constants.FLOOR_SPACING;

        return startPosition;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Vector2i getFloorOffset(int floorIndex)
    {
        return floors.get(floorIndex).getOffset();
    }

    public void calculateFloorOffsets()
    {
        // for this to work, the first floor
        // must have no offset.
        floors.get(0).getOffset().set(0, 0);

        for(int floorIndex = 1; floorIndex < floors.size; floorIndex++)
        {
            Vector3i currentStart = floors.get(floorIndex).getStart();
            Vector3i lastEnd = floors.get(floorIndex-1).getEnd();

            Vector2i offset = floors.get(floorIndex).getOffset();
            offset.set(floors.get(floorIndex - 1).getOffset());

            offset.add(lastEnd.x, lastEnd.z);
            offset.sub(currentStart.x, currentStart.z);
        }
    }
}

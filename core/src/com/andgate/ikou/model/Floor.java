package com.andgate.ikou.model;

import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;

public class Floor
{
    private TileSector masterSector;
    private Vector3i start;
    private Vector3i end;
    private Vector2i offset = new Vector2i();

    public Floor(TileSector masterSector, Vector3i start, Vector3i end)
    {
        this.masterSector = masterSector;
        this.start = start;
        this.end = end;
    }

    public TileSector getMasterSector()
    {
        return masterSector;
    }

    public Vector3i getStart()
    {
        return start;
    }

    public Vector3i getEnd()
    {
        return end;
    }

    public Vector2i getOffset()
    {
        return offset;
    }
}

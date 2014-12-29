package com.andgate.ikou.model;

import com.andgate.ikou.utility.Array2d;

public class MasterSector
{
    Array2d<TileSector> sectors;

    public MasterSector()
    {
        sectors = new Array2d<>();
    }
}
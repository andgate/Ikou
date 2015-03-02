package com.andgate.ikou.maze;

import com.andgate.ikou.model.Floor;
import com.andgate.ikou.utility.Vector2i;

public abstract class MazeParser
{
    abstract public Floor parse(MazeGenerator maze);
}
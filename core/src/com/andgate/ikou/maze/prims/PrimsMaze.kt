package com.andgate.ikou.maze.prims

import com.andgate.ikou.maze.Maze
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.math.Vector3
import java.util.*

class PrimsMaze(min_span: Int,
                max_span: Int,
                min_floors: Int,
                max_floors: Int,
                seed: Long)
: Maze(min_span, max_span, min_floors, max_floors, seed)
{
    override fun buildTileMap(): Map<Vector3, Tile>
    {
        return LinkedHashMap()
    }
}
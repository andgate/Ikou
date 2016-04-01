package com.andgate.ikou.maze.algorithm

import com.andgate.ikou.maze.MazeLayer
import com.andgate.ikou.maze.Tile
import com.andgate.ikou.utility.XorRandomGen
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

data class MazeAlgorithmInput(val width: Int,
                              val depth: Int,
                              val y: Float,
                              val lastEnd: Vector2,
                              val seed: Long)

abstract class MazeAlgorithm(val input: MazeAlgorithmInput)
{
    private val rand = XorRandomGen(input.seed)

    // These values should not be accessed until after buildTileMap is called.
    val layer: MazeLayer? = null
    val map = LinkedHashMap<Vector3, Tile>()

    // This is where the algorithm should build
    // it's internal representation
    abstract fun run()

    // This is where the algorithm should translate
    // it's internal representation into a tilemap and maze layer
    abstract fun buildTileMap()
}
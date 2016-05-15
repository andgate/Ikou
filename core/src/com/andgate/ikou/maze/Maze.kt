package com.andgate.ikou.maze

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

data class Maze(val seed_phrase: String,
                val seed: Long,
                val map: Map<Vector3, Tile>,
                val layers: Vector<MazeLayer>)

data class MazeLayer(val maze_family: String,
                     val seed: Long,
                     val start: Vector2,
                     val end: Vector2,
                     val bounds: Rectangle,
                     val y: Float)
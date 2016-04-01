/*
    This file is part of Ikou.
    Ikou is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License.
    Ikou is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with Ikou.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.andgate.ikou.maze;

import com.andgate.ikou.constants.FLOOR_SPACING
import com.andgate.ikou.maze.algorithm.MazeAlgorithm
import com.andgate.ikou.maze.algorithm.MazeAlgorithmInput
import com.andgate.ikou.maze.algorithm.depthfirst.DepthFirstMazeAlgorithm
import com.andgate.ikou.maze.algorithm.icecave.IceCaveMazeAlgorithm
import com.andgate.ikou.maze.algorithm.kruskals.KruskalsMazeAlgorithm
import com.andgate.ikou.maze.algorithm.prims.PrimsMazeAlgorithm
import com.andgate.ikou.maze.algorithm.recursivebacktracker.RecursiveBacktrackerMazeAlgorithm
import com.andgate.ikou.utility.XorRandomGen
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

class MazeFactory(val min_span: Int,
                  val max_span: Int,
                  val min_floors: Int,
                  val max_floors: Int,
                  val seed_phrase: String,
                  val seed: Long)
{
    val rand = XorRandomGen(seed)

    fun build(): Maze
    {
        val map = LinkedHashMap<Vector3, Tile>()
        val maze = Maze(seed_phrase, seed, map, Vector<MazeLayer>())


        val floorCount = rand.nextInt(min_floors, max_floors)-1
        for(floor in 0 .. floorCount)
        {
            val width = rand.nextInt(min_span, max_span)
            val depth = rand.nextInt(min_span, max_span)
            val y: Float = floor * FLOOR_SPACING
            val lastEnd = if(maze.layers.size > 1) maze.layers[floor-1].end
                          else Vector2()

            val input = MazeAlgorithmInput(width, depth, y, lastEnd, rand.next())
            val rand_maze_algo = selectRandomMazeAlgorithm(input)

            rand_maze_algo.run()
            rand_maze_algo.buildTileMap()

            map.putAll(rand_maze_algo.map)
            if(rand_maze_algo.layer != null)
                maze.layers.add(rand_maze_algo.layer)
        }

        return maze
    }

    fun selectRandomMazeAlgorithm(input: MazeAlgorithmInput): MazeAlgorithm
    {
        // Kind of a hassle, but this has to be updated everytime there is a new
        // maze algorithm.
        when(rand.nextInt(4, 4))
        {
            1 -> return DepthFirstMazeAlgorithm(input)
            2 -> return IceCaveMazeAlgorithm(input)
            3 -> return KruskalsMazeAlgorithm(input)
            4 -> return PrimsMazeAlgorithm(input)
            5 -> return RecursiveBacktrackerMazeAlgorithm(input)
            else -> UnsupportedOperationException()
        }

        // Default to a Prims maze
        return PrimsMazeAlgorithm(input)
    }
}
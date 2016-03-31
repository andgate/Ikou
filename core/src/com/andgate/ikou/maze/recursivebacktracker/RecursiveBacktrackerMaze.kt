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

package com.andgate.ikou.maze.recursivebacktracker;

import com.andgate.ikou.maze.Maze
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.math.Vector3
import java.util.*

class RecursiveBacktrackerMaze(min_span: Int,
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

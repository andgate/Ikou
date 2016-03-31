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

import com.badlogic.gdx.math.Vector3

abstract class Maze(val min_span: Int,
                    val max_span: Int,
                    val min_floors: Int,
                    val max_floors: Int,
                    val seed: Long)
{
    abstract fun buildTileMap(): Map<Vector3, Tile>
}
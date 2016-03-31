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

package com.andgate.ikou.graphics.maze

import com.andgate.ikou.constants.*
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Pool
import java.util.*

class MazeModel(val maze_map: Map<Vector3, Tile>,
                var camera: PerspectiveCamera)
: RenderableProvider, Disposable
{
    private val bmeshes = Vector<Pair<Mesh, BoundingBox>>()
    val transform = Matrix4()

    init {
        // Determine maxima and minima of the maze
        val maze_bounds = calculateMazeBounds(maze_map)
        val sectors = SectorStream(maze_map, maze_bounds)


        while(sectors.hasNext)
        {
            // Extract a map of the current sector
            val maze_sector_map = sectors.getNext()

            // Check to see if the sector actually has tiles
            if(maze_sector_map.isNotEmpty())
            {
                // Use a new MazeMesher to construct a mesh
                val mesher = MazeMesher()
                mesher.addMaze(maze_map, maze_sector_map)
                val mesh = mesher.build()

                // Save that mesh and it's bounding box
                bmeshes.add(Pair(mesh, mesh.calculateBoundingBox()))
            }
        }
    }

    override fun getRenderables(renderables: Array<Renderable>, pool: Pool<Renderable>)
    {
        for(bmesh in bmeshes)
        {
            val mesh = bmesh.first
            val bbox = bmesh.second

            if(camera.frustum.boundsInFrustum(bbox))
            {
                val renderable = pool.obtain()
                renderable.material = TILE_MATERIAL
                renderable.meshPart.offset = 0
                renderable.meshPart.size = mesh.getNumIndices()
                renderable.meshPart.primitiveType = GL20.GL_TRIANGLES
                renderable.meshPart.mesh = mesh
                renderables.add(renderable)

                renderable.worldTransform.set(transform)
            }
        }
    }

    override fun dispose()
    {
        for(bmesh in bmeshes)
        {
            bmesh.first.dispose()
        }
    }

    private fun calculateMazeBounds(maze_map: Map<Vector3, Tile>) : Pair<Vector3, Vector3>
    {
        // Determine maxima and minima of the maze
        val minima = Vector3()
        val maxima = Vector3()
        for((pos, tile) in maze_map)
        {
            minima.x = Math.min(minima.x, pos.x)
            minima.y = Math.min(minima.y, pos.y)
            minima.z = Math.min(minima.z, pos.z)

            maxima.x = Math.max(minima.x, pos.x)
            maxima.y = Math.max(minima.y, pos.y)
            maxima.z = Math.max(minima.z, pos.z)
        }

        return Pair(minima, maxima)
    }

    private class SectorStream(val maze_map: Map<Vector3, Tile>, bounds: Pair<Vector3, Vector3>)
    {
        var hasNext = true

        val minima = bounds.first
        val maxima = bounds.second

        // These are the bounds for the floor and section
        // This allows the maze model to be split into multiple meshes
        var y1: Int = bounds.first.y.toInt() // Tracks the current floor
        // Bounds for the current section
        var x1: Int = minima.x.toInt()
        var z1: Int = minima.z.toInt()
        var x2: Int = x1 + SECTOR_SPAN
        var z2: Int = z1 + SECTOR_SPAN

        fun getNext() : Map<Vector3, Tile>
        {
            val maze_sector_map = maze_map.filter { e ->
                with(e.key) {
                    (x.toInt() in x1..x2) && (z.toInt() in z1..z2) && (y.toInt() == y1)
                }
            }

            update()
            return maze_sector_map
        }

        private fun update()
        {
            // Update the current
            x1 += SECTOR_SPAN
            z1 += SECTOR_SPAN
            x2 += SECTOR_SPAN
            z2 += SECTOR_SPAN

            if(x2 > maxima.x && z2 > maxima.z)
            {
                ++y1
                x1 = minima.x.toInt()
                z1 = minima.z.toInt()
                x2 = x1 + SECTOR_SPAN
                z2 = z1 + SECTOR_SPAN
            }

            if(y1 > maxima.y) hasNext = false
        }
    }
}

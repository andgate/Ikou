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
import com.andgate.ikou.maze.Maze
import com.andgate.ikou.maze.MazeLayer
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.Gdx
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

class MazeModel(val maze: Maze,
                var cam: PerspectiveCamera)
: RenderableProvider, Disposable
{
    private val bmeshes = Vector<Pair<Mesh, BoundingBox>>()
    val transform = Matrix4()

    init {
        val sectors = SectorStream(maze)
        while(sectors.hasNext)
        {
            // Extract a map of the current sector
            val maze_sector_map = sectors.getNext()

            // Check to see if the sector actually has tiles
            if(maze_sector_map.isNotEmpty())
            {
                // Use a new MazeMesher to construct a mesh
                val mesher = MazeMesher()
                mesher.addMaze(maze.map, maze_sector_map)
                val mesh = mesher.build()

                // Save that mesh and it's bounding box
                bmeshes.add(Pair(mesh, mesh.calculateBoundingBox()))
            }
        }
    }

    override fun getRenderables(renderables: Array<Renderable>, pool: Pool<Renderable>)
    {
        for(i in bmeshes.indices)
        {
            val bmesh = bmeshes[i]
            val mesh = bmesh.first
            val bbox = bmesh.second

            if(cam.frustum.boundsInFrustum(bbox))
            {
                val renderable = pool.obtain()
                renderable.material = TILE_MATERIAL
                renderable.meshPart.offset = 0
                renderable.meshPart.size = mesh.numIndices
                renderable.meshPart.primitiveType = GL20.GL_TRIANGLES
                renderable.meshPart.mesh = mesh
                renderables.add(renderable)

                renderable.worldTransform.set(transform)
            }
        }
    }

    override fun dispose()
    {
        for(i in bmeshes.indices)
        {
            bmeshes[i].first.dispose()
        }
    }

    private class SectorStream(val maze: Maze)
    {
        private val TAG: String = "SectorStream"

        // Flag to determine if the stream is active
        var hasNext = true

        var layerIndex = 0
        var layer: MazeLayer

        // These are the bounds for a section of the current layer
        // This allows the maze model to be split into multiple meshes
        var x1: Int
        var x2: Int
        var z1: Int
        var z2: Int

        init
        {
            if ( maze.layers.isEmpty() )
                Gdx.app.log(TAG, "Sector Stream cannot operate on empty maze", UnsupportedOperationException())

            layer = maze.layers[layerIndex]
            x1 = layer.bounds.x.toInt()
            z1 = layer.bounds.y.toInt()
            x2 = x1 + SECTOR_SPAN
            z2 = z1 + SECTOR_SPAN
        }

        fun getNext() : Map<Vector3, Tile>
        {
            // Filter out a sector by the current bounds
            val maze_sector_map = maze.map.filter { e ->
                with(e.key) {
                    (x.toInt() in x1..x2) && (z.toInt() in z1..z2) && (y == layer.y)
                }
            }

            // Grab the next bounds
            update()
            return maze_sector_map
        }

        private fun update()
        {
            // First move the sector along x
            x1 += SECTOR_SPAN
            x2 += SECTOR_SPAN

            // If the sector goes out of the x bounds..
            val max_x = with(layer.bounds) { x + width }
            if(x1 > max_x)
            {
                // Then move the z bounds
                z1 += SECTOR_SPAN
                z2 += SECTOR_SPAN

                // If after moving z, if the sector goes out of the z bounds...
                val max_z = with(layer.bounds) { y + height }
                if(z1 > max_z)
                {
                    // ..move to the next layer...
                    ++layerIndex
                    if(layerIndex < maze.layers.size)
                        layer = maze.layers[layerIndex]
                    else { // If there are no more layers, end the stream
                        hasNext = false
                        return
                    }

                    // ..and reset the z bounds.
                    z1 = layer.bounds.y.toInt()
                    z2 = z1 + SECTOR_SPAN
                }

                // Reset the x bounds last
                // in case the layer has been bumped during the z bounds check.
                x1 = layer.bounds.x.toInt()
                x2 = x1 + SECTOR_SPAN
            }
        }
    }
}

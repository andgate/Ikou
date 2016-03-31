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

package com.andgate.ikou.graphics.maze;

import com.andgate.ikou.constants.*
import com.andgate.ikou.graphics.util.CubeMesher;
import com.andgate.ikou.maze.Tile
import com.badlogic.gdx.math.Vector3

class MazeMesher : CubeMesher()
{
    fun addMaze(maze_map: Map<Vector3, Tile>,
                maze_sector_map: Map<Vector3, Tile>)
    {
        for((pos, tile) in maze_sector_map)
        {
            addTile(maze_map, pos, tile)
        }
    }

    fun addTile(maze_map: Map<Vector3, Tile>, pos: Vector3, tile: Tile)
    {
        var x = pos.x + TILE_HALF_SPAN
        var y = pos.y
        var z = pos.z + TILE_HALF_SPAN
        var width = pos.x - TILE_HALF_SPAN
        var height = TILE_HEIGHT
        var depth = pos.z - TILE_HALF_SPAN

        if(tile.type == Tile.Type.OBSTACLE) height *= 2f

        calculateVerts(x, y, z, TILE_HALF_SPAN, height, TILE_HALF_SPAN)
        // Only the top and bottom are ever visible
        addTop(tile.colorOf())
        addBottom(OBSTACLE_TILE_COLOR)
        // Walls border the sides
        addWalls(maze_map, pos, tile)
    }

    private fun addWalls(maze_map: Map<Vector3, Tile>, pos: Vector3, tile: Tile)
    {
        // Determine if an obstacle tile is obstructing part of the wall
        val bordersObstacle = tile.type == Tile.Type.OBSTACLE

        // Check for front neighbor
        if (!maze_map.contains(Vector3(pos.x, pos.y, pos.z + 1)))
        {
            val leftCorner = getFrontLeftCorner(maze_map, pos)
            val rightCorner = getFrontRightCorner(maze_map, pos)
            addFrontWall(leftCorner, rightCorner, bordersObstacle, pos)
        }

        // Check for back neighbor
        if (!maze_map.contains(Vector3(pos.x, pos.y, pos.z - 1)))
        {
            val leftCorner = getBackLeftCorner(maze_map, pos)
            val rightCorner = getBackRightCorner(maze_map, pos)
            addBackWall(leftCorner, rightCorner, bordersObstacle, pos)
        }

        // Check for left neighbor
        if (!maze_map.contains(Vector3(pos.x - 1, pos.y, pos.z)))
        {
            val frontCorner = getFrontLeftCorner(maze_map, pos)
            val backCorner = getBackLeftCorner(maze_map, pos)
            addLeftWall(frontCorner, backCorner, bordersObstacle, pos)
        }

        // Check for right neighbor
        if (!maze_map.contains(Vector3(pos.x + 1, pos.y, pos.z)))
        {
            val frontCorner = getFrontRightCorner(maze_map, pos)
            val backCorner = getBackRightCorner(maze_map, pos)
            addRightWall(frontCorner, backCorner, bordersObstacle, pos)
        }
    }

    // This gets REALLY ugly. I'm not proud of it.
    // Unfortunantly, there are just too many little differences for
    // each wall they each one needed it's own, special routine.
    // The worst part about it, is the routines are very similar.

    private enum class WallCorner { None, Inside, Outside }

    private fun getFrontLeftCorner(maze_map: Map<Vector3, Tile>, pos: Vector3): WallCorner
    {
        // Assume this is an outside (270 degree) corner
        var corner = WallCorner.Outside

        // Check if there is a connecting neighbor that forms an inside (90 degree) corner
        if(maze_map.contains(Vector3(pos.x-1, pos.y, pos.z+1)))
            corner = WallCorner.Inside
        // Otherwise, check to see if there is an adjacent neighbor (no corner at all)
        else if(maze_map.contains(Vector3(pos.x-1, pos.y, pos.z)))
            corner = WallCorner.None

        return corner
    }

    private fun getFrontRightCorner(maze_map: Map<Vector3, Tile>, pos: Vector3): WallCorner
    {
        var corner = WallCorner.Outside

        // Check if there is a connecting neighbor that forms an inside (90 degree) corner
        if(maze_map.contains(Vector3(pos.x+1, pos.y, pos.z+1)))
            corner = WallCorner.Inside
        // Otherwise, check to see if there is an adjacent neighbor (no corner at all)
        else if(maze_map.contains(Vector3(pos.x+1, pos.y, pos.z)))
            corner = WallCorner.None

        return corner
    }

    private fun getBackLeftCorner(maze_map: Map<Vector3, Tile>, pos: Vector3): WallCorner
    {
        var corner = WallCorner.Outside

        // Check if there is a connecting neighbor that forms an inside (90 degree) corner
        if(maze_map.contains(Vector3(pos.x-1, pos.y, pos.z-1)))
            corner = WallCorner.Inside
        // Otherwise, check to see if there is an adjacent neighbor (no corner at all)
        else if(maze_map.contains(Vector3(pos.x-1, pos.y, pos.z)))
            corner = WallCorner.None

        return corner
    }

    private fun getBackRightCorner(maze_map: Map<Vector3, Tile>, pos: Vector3): WallCorner
    {
        var corner = WallCorner.Outside

        // Check if there is a connecting neighbor that forms an inside (90 degree) corner
        if(maze_map.contains(Vector3(pos.x+1, pos.y, pos.z-1)))
            corner = WallCorner.Inside
        // Otherwise, check to see if there is an adjacent neighbor (no corner at all)
        else if(maze_map.contains(Vector3(pos.x+1, pos.y, pos.z)))
            corner = WallCorner.None

        return corner
    }

    private fun addFrontWall(leftCorner: WallCorner, rightCorner: WallCorner, bordersObstacle: Boolean, pos: Vector3)
    {
        var width: Float = TILE_CELL_SPAN
        var height: Float = WALL_HEIGHT
        val depth: Float = WALL_THICKNESS
        var x: Float = pos.x
        var y: Float = pos.y
        val z: Float = pos.z + TILE_CELL_SPAN

        when(leftCorner)
        {
            WallCorner.Inside -> {
                width -= WALL_THICKNESS
                x += WALL_THICKNESS
            }
            WallCorner.Outside -> {
                width += WALL_THICKNESS
                x -= WALL_THICKNESS
            }
            WallCorner.None -> {}
        }

        when(rightCorner)
        {
            WallCorner.Inside ->
                width -= WALL_THICKNESS;
            WallCorner.Outside ->
                width += WALL_THICKNESS;
            WallCorner.None -> {}
        }

        calculateVerts(x, y, z, width, height, depth);

        addTop(TILE_WALL_COLOR);
        addBottom(TILE_WALL_COLOR);
        addFront(TILE_WALL_COLOR);

        if(rightCorner == WallCorner.Outside)
            addRight(TILE_WALL_COLOR);

        if(leftCorner == WallCorner.Outside)
            addLeft(TILE_WALL_COLOR);

        if(!bordersObstacle)
        {
            x = pos.x;
            width = TILE_CELL_SPAN;
            height /= 2.0f;
            y += height;
            calculateVerts(x, y, z, width, height, depth);
            addBack(TILE_WALL_COLOR);
        }
    }

    private fun addBackWall(leftCorner: WallCorner, rightCorner: WallCorner, bordersObstacle: Boolean, pos: Vector3)
    {
        var width: Float = TILE_CELL_SPAN
        var height: Float = WALL_HEIGHT
        val depth: Float = WALL_THICKNESS
        var x: Float = pos.x
        var y: Float = pos.y
        val z: Float = pos.z - depth

        when(leftCorner)
        {
            WallCorner.Inside -> {
                width -= WALL_THICKNESS
                x += WALL_THICKNESS
            }
            WallCorner.Outside -> {
                width += WALL_THICKNESS
                x -= WALL_THICKNESS
            }
            WallCorner.None -> {}
        }

        when(rightCorner)
        {
            WallCorner.Inside ->
                width -= WALL_THICKNESS
            WallCorner.Outside ->
                width += WALL_THICKNESS
            WallCorner.None -> {}
        }

        calculateVerts(x, y, z, width, height, depth)

        addTop(TILE_WALL_COLOR)
        addBottom(TILE_WALL_COLOR)
        addBack(TILE_WALL_COLOR)

        if(rightCorner == WallCorner.Outside)
            addRight(TILE_WALL_COLOR)

        if(leftCorner == WallCorner.Outside)
            addLeft(TILE_WALL_COLOR)

        if(!bordersObstacle)
        {
            x = pos.x
            width = TILE_CELL_SPAN
            height /= 2.0f
            y += height
            calculateVerts(x, y, z, width, height, depth)
            addFront(TILE_WALL_COLOR)
        }
    }

    private fun addLeftWall(frontCorner: WallCorner, backCorner: WallCorner, bordersObstacle: Boolean, pos: Vector3)
    {
        val width: Float = WALL_THICKNESS
        var height: Float = WALL_HEIGHT
        var depth: Float = TILE_CELL_SPAN
        val x: Float = pos.x - width
        var y: Float = pos.y
        var z: Float = pos.z

        calculateVerts(x, y, z, width, height, depth);

        addTop(TILE_WALL_COLOR);
        addBottom(TILE_WALL_COLOR);

        if(frontCorner == WallCorner.Inside)
        {
            addFront(TILE_WALL_COLOR)
            depth -= WALL_THICKNESS
        }

        if(backCorner == WallCorner.Inside)
        {
            addBack(TILE_WALL_COLOR)
            depth -= WALL_THICKNESS
            z += WALL_THICKNESS
        }

        calculateVerts(x, y, z, width, height, depth);
        addLeft(TILE_WALL_COLOR);

        if(!bordersObstacle)
        {
            height /= 2.0f;
            depth = TILE_CELL_SPAN;
            y += height;
            z = pos.z;
            calculateVerts(x, y, z, width, height, depth);
            addRight(TILE_WALL_COLOR);
        }
    }

    private fun addRightWall(frontCorner: WallCorner, backCorner: WallCorner, bordersObstacle: Boolean, pos: Vector3)
    {
        val width: Float = WALL_THICKNESS
        var height: Float = WALL_HEIGHT
        var depth: Float = TILE_CELL_SPAN
        val x: Float = pos.x + TILE_CELL_SPAN
        var y: Float = pos.y
        var z: Float = pos.z

        calculateVerts(x, y, z, width, height, depth);

        addTop(TILE_WALL_COLOR);
        addBottom(TILE_WALL_COLOR);

        if(frontCorner == WallCorner.Inside)
        {
            addFront(TILE_WALL_COLOR);
            depth -= WALL_THICKNESS;
        }

        if(backCorner == WallCorner.Inside)
        {
            addBack(TILE_WALL_COLOR);
            depth -= WALL_THICKNESS;
            z += WALL_THICKNESS;
        }

        calculateVerts(x, y, z, width, height, depth);
        addRight(TILE_WALL_COLOR);

        if(!bordersObstacle)
        {
            height /= 2.0f;
            depth = TILE_CELL_SPAN;
            y += height;
            z = pos.z;
            calculateVerts(x, y, z, width, height, depth);
            addLeft(TILE_WALL_COLOR);
        }
    }
}

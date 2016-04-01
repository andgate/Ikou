package com.andgate.ikou.maze.algorithm.prims

import com.andgate.ikou.maze.Tile
import com.andgate.ikou.maze.MazeLayer
import com.andgate.ikou.maze.algorithm.MazeAlgorithm
import com.andgate.ikou.maze.algorithm.MazeAlgorithmInput
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

class PrimsMazeAlgorithm(input: MazeAlgorithmInput)
: MazeAlgorithm(input)
{
    private val TAG: String = "PrimsMazeAlgorithm"
    private val FAMILY: String = "Prims"

    private class Cell(val pos: Vector2)
    {
        var visited: Boolean = false
        var edges = Vector<Edge>()

        // Use for running cell sweeps
        var leftEdge: Edge? = null
        var rightEdge: Edge? = null
        var topEdge: Edge? = null
        var bottomEdge: Edge? = null

        fun connectLeft(other: Cell): Edge
        {
            val edge = connect(other)
            this.leftEdge = edge
            other.rightEdge = edge
            return edge
        }

        fun connectRight(other: Cell): Edge
        {
            return other.connectLeft(this)
        }

        fun connectBottom(other: Cell): Edge
        {
            val edge = connect(other)
            this.bottomEdge = edge
            other.topEdge = edge
            return edge
        }

        fun connectTop(other: Cell): Edge
        {
            return other.connectBottom(this)
        }

        fun connect(other: Cell): Edge
        {
            val edge = Edge(this, other)
            this.edges.add(edge)
            other.edges.add(edge)

            return edge
        }
    }

    private class Edge(var a: Cell, var b: Cell)
    {
        var isWall = true
    }

    // Start with a 2d-array of unconnected cells
    private val cells = Array( input.width, { x -> Array(input.depth, { y -> Cell(Vector2(x.toFloat(), y.toFloat())) }) } )
    private val frontier = Vector<Edge>()

    private val start = Vector2()

    override fun run()
    {
        // Connect the cells together
        connectCells()

        // Pick a random point as the starting point
        val startX = rand.nextInt(0, input.width - 1)
        val startY = rand.nextInt(0, input.depth - 1)
        // Set that point as visited
        cells[startX][startY].visited = true
        // Store the walls of the random point, all edges should be walls at this point
        frontier.addAll(cells[startX][startY].edges)
        // Store the starting position
        start.x = startX.toFloat(); start.y = startY.toFloat()

        // While there are still walls in the frontier
        while(frontier.isNotEmpty())
        {
            // Pick a random wall from the frontier
            val pickIndex = rand.nextInt(0, frontier.size-1)
            val pick = frontier[pickIndex]

            // If there exists exactly one visited wall on either side of the picked edge
            if ((!pick.a.visited xor !pick.b.visited)){
                // Mark the pick a passage
                pick.isWall = false
                // Grab the unvisited cell
                val unvisited_cell = if(pick.b.visited) pick.a else pick.b
                // Add the unvisited cells walls to the frontier
                storeWalls(unvisited_cell)
                // Mark the unvisited cell as visited
                unvisited_cell.visited = true
            }

            // After checking the edge, remove it from the frontier
            frontier.removeAt(pickIndex)
        }
    }

    override fun buildTileMap()
    {
        // Calculate the end point
        val end: Vector2 = calculateEnd()

        // Convert the start coordinates from prim maze to tile maze
        start.x = start.x * 2f - 1f
        start.y = start.y * 2f - 1f

        //
        // calculate offset from lastEnd - start, and set start to lastEnd
        val offset = Vector2(input.lastEnd).sub(start)

        // Update the end and set the offset
        start.x = input.lastEnd.x
        start.y = input.lastEnd.y
        end.x = end.x * 2f - 1f + offset.x
        end.y = end.y * 2f - 1f + offset.y

        // calculate tile map width and depth, use the offset as the minima of the tiles
        val tileMapWidth: Float = 2f * input.width - 1f
        val tileMapDepth: Float = 2f * input.depth - 1f
        val bounds = Rectangle(offset.x, offset.y, tileMapWidth, tileMapDepth)

        for(x in 0 .. (cells.size - 1))
        {
            for(y in 0 .. (cells[x].size-1))
            {
                // Generate the cell
                // Needs to take account of the offset as well...
                val tileX: Float = 2f*(x+1f) - 1f + bounds.x
                val tileZ: Float = 2f*(y+1f) - 1f + bounds.y

                val cell = cells[x][y]
                if(countPaths(cell) > 2)
                    map.put(Vector3(tileX, input.y, tileZ), Tile(Tile.Type.STICKY))
                else
                    map.put(Vector3(tileX, input.y, tileZ), Tile(Tile.Type.SMOOTH))

                // Generate the right edge's cell
                if(cell.rightEdge != null)
                {
                    if(cell.rightEdge?.isWall == false)
                        map.put(Vector3(tileX+1f, input.y, tileZ), Tile(Tile.Type.SMOOTH))
                    else if (rand.nextBool())
                        map.put(Vector3(tileX+1f, input.y, tileZ), Tile(Tile.Type.OBSTACLE))
                }
                // Generate the bottom edge's cell
                if(cell.bottomEdge != null)
                {
                    if(cell.bottomEdge?.isWall == false)
                        map.put(Vector3(tileX, input.y, tileZ+1f), Tile(Tile.Type.SMOOTH))
                    else if (rand.nextBool())
                        map.put(Vector3(tileX, input.y, tileZ+1f), Tile(Tile.Type.OBSTACLE))
                }

                // Generate the bottom right tile
                if(cell.bottomEdge != null && cell.rightEdge != null && rand.nextBool())
                    map.put(Vector3(tileX+1f, input.y, tileZ+1f), Tile(Tile.Type.OBSTACLE))
            }
        }

        map.put(Vector3(end.x, input.y, end.y), Tile(Tile.Type.DROP))

        layer = MazeLayer(FAMILY, input.seed, start, end, bounds, input.y)
    }

    private fun connectCells()
    {
        for(x in 0 .. (cells.size - 1))
        {
            for(y in 0 .. (cells[x].size-1))
            {
                // Connect to right neighbor
                if(cells.size > x+1 && cells[x+1].size > y)
                    cells[x][y].connectRight(cells[x+1][y])
                // Connect to bottom neighbor
                if(cells[x].size > y+1)
                    cells[x][y].connectBottom(cells[x][y+1])
            }
        }
    }

    private fun storeWalls(cell: Cell)
    {
        for(edge in cell.edges)
            if(edge.isWall) frontier.add(edge)
    }

    private fun countPaths(cell: Cell): Int
    {
        var count: Int = 0
        for(edge in cell.edges) {
            if (!edge.isWall) ++count
        }

        return count
    }

    private fun calculateEnd(): Vector2
    {

        val mazeDiagLength: Float
                = with(input) { Math.sqrt((width*width + depth*depth).toDouble()).toFloat() }

        // If the end is close to the start, walk again
        val end = Vector2()
        do{
            end.x = rand.nextInt(0, input.width-1).toFloat()
            end.y = rand.nextInt(0, input.depth-1).toFloat()
        } while(end.dst(start) <= mazeDiagLength/4f)

        return end
    }

    private fun randomWalk(steps: Int): Vector2
    {
        // Get the start cell
        var cell = cells[start.x.toInt()][start.y.toInt()]
        for(i in 1..steps)
        {
            // Pick a random wall without an edge
            val path = pickRandomPath(cell)
            // Set cell to it's neighbor using the path
            cell = if(cell == path.a) path.b else path.a
        }

        // Return the final destination of the random walk
        return cell.pos
    }

    private fun pickRandomPath(cell: Cell): Edge
    {
        // pick a random edge
        var edge = cell.edges[rand.nextInt(0, cell.edges.size-1)]
        // If that edge is a wall, keep picking until a path is chosen
        while(edge.isWall == true)
            edge = cell.edges[rand.nextInt(0, cell.edges.size-1)]
        // Return an edge from the cell that is a path
        return edge
    }
}
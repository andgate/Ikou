package com.andgate.ikou.maze;

import com.andgate.ikou.utility.Vector2i;

import java.util.LinkedList;

/**
 * Created by Gabe on 2/28/2015.
 */
public class RecursiveBacktrackerMazeGenerator extends MazeGenerator
{
    public RecursiveBacktrackerMazeGenerator(int width, int height, int startX, int startY, int endX, int endY, long seed)
    {
        super(new PerfectMazeParser(), width, height, startX, startY, endX, endY, seed);
    }

    public RecursiveBacktrackerMazeGenerator(int width, int height, int startX, int startY, int endX, int endY)
    {
        super(new PerfectMazeParser(), width, height, startX, startY, endX, endY);
    }

    protected void generateMaze()
    {
        boolean[][] cells = new boolean[size.y+1][size.x+1];
        LinkedList<Vector2i> stack = new LinkedList<>();

        // A cell is a location in the maze.
        Vector2i cell = new Vector2i(start);
        stack.addFirst(cell);
        LinkedList<Direction> neighborhood = new LinkedList<>();

        do {
            // Mark current cell as visited.
            cells[cell.y][cell.x] = true;

            // Examine current cell's neighborhood
            getFreeNeighbors(cells, cell.x, cell.y, neighborhood);

            // Pick a random free neighbor
            if(neighborhood.size() > 0)
            {
                stack.addFirst(cell);
                cell = new Vector2i(cell.x, cell.y);

                int index = random.nextInt(neighborhood.size());
                Direction carveDirection = neighborhood.get(index);

                carve(cell.x, cell.y, carveDirection);

                Vector2i nextCellCoord = getNextCoordInDirection(cell.x, cell.y, carveDirection);
                cell.x = nextCellCoord.x;
                cell.y = nextCellCoord.y;
            }
            else
            {
                stack.removeFirst();
                if(stack.size() > 0)
                    cell = stack.getFirst();
            }


        } while(!stack.isEmpty());

    }

    /**
     *
     * @param cells The walls in the maze
     * @param x The current cell's x-coord
     * @param y The current cell's y-coord
     * @param neighborhood The place to store the neighborhood
     */
    private void getFreeNeighbors(final boolean[][] cells, final int x, int y, LinkedList<Direction> neighborhood)
    {
        // clear the given neighborhood, as it needs to be overwritten
        neighborhood.clear();

        if( (y-1) >= 0 && !cells[y-1][x] && isWallSideFull(x, y, Direction.Up) && isWallPresent(x, y-2))
        {
            neighborhood.add(Direction.Up);
        }

        if( (y+1) < cells.length && !cells[y+1][x] && isWallSideFull(x, y, Direction.Down) && isWallPresent(x, y+2))
        {
            neighborhood.add(Direction.Down);
        }

        if( (x-1) >= 0 && !cells[y][x-1] && isWallSideFull(x, y, Direction.Left) && isWallPresent(x-2, y))
        {
            neighborhood.add(Direction.Left);
        }

        if( (x+1) < cells[y].length && !cells[y][x+1] && isWallSideFull(x, y, Direction.Right) && isWallPresent(x+2, y))
        {
            neighborhood.add(Direction.Right);
        }
    }
}

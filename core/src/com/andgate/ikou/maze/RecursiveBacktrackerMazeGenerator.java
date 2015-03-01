package com.andgate.ikou.maze;

import com.andgate.ikou.utility.Vector2i;

import java.util.LinkedList;

/**
 * Created by Gabe on 2/28/2015.
 */
public class RecursiveBacktrackerMazeGenerator extends MazeGenerator
{
    private int startX, startY;

    public RecursiveBacktrackerMazeGenerator(int width, int height)
    {
        super(width, height);

        this.startX = random.nextInt(width);
        this.startY = random.nextInt(height);
    }

    public RecursiveBacktrackerMazeGenerator(int width, int height, int startX, int startY)
    {
        super(width, height);

        checkLocation(startX, startY);

        this.startX = startX;
        this.startY = startY;
    }

    protected void generateMaze()
    {
        int height = getHeight();
        int width = getWidth();

        boolean[][] cells = new boolean[height+1][width+1];
        LinkedList<Cell> stack = new LinkedList<Cell>();

        Cell cell = new Cell(startX, startY);
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
                cell = new Cell(cell.x, cell.y);

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
     * @param cells The population of cells
     * @param x The current cell's x-coord
     * @param y The current cell's y-coord
     * @param neighborhood The place to store the neighborhood
     */
    private void getFreeNeighbors(final boolean[][] cells, final int x, int y, LinkedList<Direction> neighborhood)
    {
        // clear the given neighborhood, as it needs to be overwritten
        neighborhood.clear();

        if( (y-1) > 0 && !cells[y-1][x] && !cells[y-2][x])
        {
            neighborhood.add(Direction.Up);
        }

        if( (y+1) < cells.length-1 && !cells[y+1][x] && !cells[y+2][x] )
        {
            neighborhood.add(Direction.Down);
        }

        if( (x-1) > 0 && !cells[y][x-1] && !cells[y][x-2])
        {
            neighborhood.add(Direction.Left);
        }

        if( (x+1) < cells[y].length-1 && !cells[y][x+1] && !cells[y][x+2] )
        {
            neighborhood.add(Direction.Right);
        }
    }
}

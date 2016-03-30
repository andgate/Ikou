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

import com.andgate.ikou.utility.Vector2i;

import java.util.LinkedList;

class RecursiveBacktrackerMazeGenerator : MazeGenerator()
{
    public RecursiveBacktrackerMazeGenerator(int width, int height, int startX, int startY, int endX, int endY, long seed)
    {
        super(new PerfectMazeParser(), width, height, startX, startY, endX, endY, seed);
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

                int index = random.nextInt(0, neighborhood.size());
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

        getWalls()[start.y][start.x] = false;
        getWalls()[end.y][end.x] = false;

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

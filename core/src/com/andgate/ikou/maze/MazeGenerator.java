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

import com.andgate.ikou.model.Floor;
import com.andgate.ikou.utility.Vector2i;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

public abstract class MazeGenerator
{
    protected final Vector2i start = new Vector2i();
    protected final Vector2i end = new Vector2i();
    protected final Vector2i size = new Vector2i();

    protected Random random = new Random();

    public enum Direction {Up, Down, Left, Right }
    protected final static Direction[] directions = Direction.values();

    MazeParser parser;

    // Walls are turned on and off,
    // stored as walls[y][x]
    private boolean[][] walls;

    protected MazeGenerator(MazeParser parser, int width, int height, int startX, int startY, int endX, int endY, long seed)
    {
        this(parser, width, height, startX, startY, endX, endY);
        random.setSeed(seed);
    }

    protected MazeGenerator(MazeParser parser, int width, int height, int startX, int startY, int endX, int endY)
    {
        this.parser = parser;

        checkSize(width, height);
        size.set(width, height);

        checkLocation(startX, startY);
        checkLocation(endX, endY);
        start.set(startX, startY);
        end.set(endX, endY);

        walls = new boolean[height+1][width+1];

        reset();
    }

    public final void reset()
    {
        for(int i = 0; i < walls.length; i++)
        {
            Arrays.fill(walls[i], true);
        }

        walls[start.y][start.x] = false;
    }

    public final void generate()
    {
        reset();
        generateMaze();
    }

    protected abstract void generateMaze();

    protected Direction randomDirection()
    {
        int pick = random.nextInt(directions.length);
        return directions[pick];
    }

    protected void checkSize(int width, int height)
    {
        if(width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException("Size must be positive.");
        }
    }

    protected boolean isInBounds(int x, int y)
    {
        return (0 <= x && x <= size.x) && (0 <= y && y <= size.y);
    }

    protected void checkLocation(int x, int y)
    {
        if(x < 0 || size.x < x)
        {
            throw new IndexOutOfBoundsException("X out of range: " + x);
        }

        if(y < 0 || size.y < y)
        {
            throw new IndexOutOfBoundsException("Y out of range: " + y);
        }
    }

    protected boolean carve(int x, int y, Direction direction)
    {
        checkLocation(x, y);

        Vector2i location = getNextCoordInDirection(x, y, direction);

        boolean beforeCarve = walls[location.y][location.x];
        walls[location.y][location.x] = false;
        return beforeCarve;
    }

    public boolean isWallPresent(int x, int y)
    {
        boolean wallExists = isInBounds(x, y);

        if(wallExists)
        {
            return walls[y][x];
        }

        // If there is no wall, it is the edge of the maze,
        // and there is a wall there.
        // The carving algorithm has to detect the edge.
        return true;
    }

    public boolean isWallSideFull(int x, int y, Direction direction)
    {
        boolean isFull = true;

        switch(direction)
        {
            case Up:
                isFull = isFull && isWallPresent(x-1, y-1);
                isFull = isFull && isWallPresent(x, y-1);
                isFull = isFull && isWallPresent(x+1, y-1);
                break;
            case Down:
                isFull = isFull && isWallPresent(x-1, y+1);
                isFull = isFull && isWallPresent(x, y+1);
                isFull = isFull && isWallPresent(x+1, y+1);
                break;
            case Left:
                isFull = isFull && isWallPresent(x-1, y-1);
                isFull = isFull && isWallPresent(x-1, y);
                isFull = isFull && isWallPresent(x-1, y+1);
                break;
            case Right:
                isFull = isFull && isWallPresent(x+1, y-1);
                isFull = isFull && isWallPresent(x+1, y);
                isFull = isFull && isWallPresent(x+1, y+1);
                break;
            default:
                break;
        }

        return isFull;
    }

    boolean[][] getNeighborhood(int x, int y)
    {
        boolean[][] neighborhood = new boolean[3][3];

        int currY = y - 1;
        for(int i = 0; i < 3; i++)
        {
            int currX = x - 1;
            for(int j = 0; j < 3; j++)
            {
                neighborhood[i][j] = walls[currY][currX];
                ++currX;
            }

            ++currY;
        }

        return neighborhood;
    }

    public int countWalls(int x, int y)
    {
        int count = 0;

        if(isWallPresent(x-1, y)) ++count;
        if(isWallPresent(x+1, y)) ++count;
        if(isWallPresent(x, y-1)) ++count;
        if(isWallPresent(x, y+1)) ++count;

        return count;
    }

    Vector2i tmpVec = new Vector2i();

    /**
     * Takes some coordinate and finds the state of the next wall in the given direction.
     * @param x The x-coord of the starting location
     * @param y The y-coord of the starting location
     * @param direction The direction of the next tile
     * @return A temporary vector that contains the coords of the destination tile.
     */
    protected Vector2i getNextCoordInDirection(int x, int y, Direction direction)
    {
        tmpVec.set(x, y);

        switch(direction)
        {
            case Up:
                tmpVec.y--;
                break;
            case Down:
                tmpVec.y++;
                break;
            case Left:
                tmpVec.x--;
                break;
            case Right:
                tmpVec.x++;
                break;
            default:
                break;
        }

        return tmpVec;
    }

    public Vector2i getSize() { return size; }
    public Vector2i getStart() { return start; }
    public Vector2i getEnd() { return end; }
    public boolean[][] getWalls()
    {
        return walls;
    }

    private static final String WALL_CHAR = "#";
    private static final String FLOOR_CHAR = " ";

    public void print(PrintStream out)
    {
        for(int i = 0; i < walls.length; i++)
        {
            for(int j = 0; j < walls[i].length; j++)
            {
                if(walls[i][j])
                    out.print(WALL_CHAR);
                else
                    out.print(FLOOR_CHAR);
            }

            out.println();
        }
    }

    public Floor computeFloor()
    {
        return parser.parse(this);
    }
}
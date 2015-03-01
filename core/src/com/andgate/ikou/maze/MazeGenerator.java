package com.andgate.ikou.maze;

import com.andgate.ikou.utility.Vector2i;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

public abstract class MazeGenerator
{
    private int width, height;
    protected Random random = new Random();

    public enum Direction {Up, Down, Left, Right }
    protected final static Direction[] directions = Direction.values();

    // Walls are turned on and off,
    // stored as walls[y][x]
    private boolean[][] walls;

    protected static class Cell
    {
        protected int x, y;

        protected Cell(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public String toString()
        {
            return "(" + x + ", " + y + ")";
        }
    }

    protected MazeGenerator(int width, int height, long seed)
    {
        this(width, height);
        random.setSeed(seed);
    }

    protected MazeGenerator(int width, int height)
    {
        if(width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException("Size must be positive.");
        }

        this.width = width;
        this.height = height;

        walls = new boolean[height+1][width+1];

        reset();
    }

    public final void reset()
    {
        for(int i = 0; i < walls.length; i++)
        {
            Arrays.fill(walls[i], true);
        }
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

    protected void checkLocation(int x, int y)
    {
        if(x < 0 || width < x)
        {
            throw new IndexOutOfBoundsException("X out of range: " + x);
        }

        if(y < 0 || height < y)
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

    protected boolean isWallPresent(int x, int y, Direction direction)
    {
        checkLocation(x, y);

        Vector2i location = getNextCoordInDirection(x, y, direction);

        if(location.x >= width || location.y >= height)
            return false; // outside the bounds is nothing but wall

        return walls[location.y][location.x];
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

    public int getWidth() { return width; }

    public int getHeight() { return height; }

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
}
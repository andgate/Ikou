package com.andgate.ikou.maze;

import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.MasterSector;
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;

public class PerfectMazeParser extends MazeParser
{
    Vector3i start = new Vector3i();
    Vector3i end = new Vector3i();

    @Override
    public Floor parse(MazeGenerator maze)
    {
        TilePalette palette = new TilePalette();
        MasterSector master = buildMasterSector(maze);

        start.x = maze.getStart().x;
        start.z = maze.getStart().y;

        end.x = maze.getEnd().x;
        end.z = maze.getEnd().y;

        master.setStack(new TileStack(Tile.End), end.x, end.z);

        return new Floor(master, palette, start, end);
    }

    private MasterSector buildMasterSector(MazeGenerator maze)
    {
        MasterSector masterSector = new MasterSector();
        boolean[][] walls = maze.getWalls();

        for(int y = 0; y < walls.length; y++)
        {
            for(int x = 0; x < walls[y].length; x++)
            {
                if(maze.isWallPresent(x,y))
                {
                    if(maze.random.nextInt(2) == 0)
                        masterSector.setStack(new TileStack(Tile.Obstacle), x, y);
                    else
                        masterSector.setStack(new TileStack(Tile.Blank), x, y);
                }
                else if(maze.countWalls(x,y) < 2)
                {
                    masterSector.setStack(new TileStack(Tile.Rough), x, y);
                }
                else
                {
                    masterSector.setStack(new TileStack(Tile.Smooth), x, y);
                }
            }
        }

        return masterSector;
    }
}

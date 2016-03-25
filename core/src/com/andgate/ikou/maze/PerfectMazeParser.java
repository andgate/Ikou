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
import com.andgate.ikou.model.MasterSector;
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.model.TileStack.Tile;
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

        master.setStack(new TileStack(Tile.Type.End), end.z, end.x);

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
                        masterSector.setStack(new TileStack(Tile.Obstacle), y, x);
                    else
                        masterSector.setStack(new TileStack(Tile.Blank), y, x);
                }
                else if(maze.countWalls(x,y) < 2)
                {
                    masterSector.setStack(new TileStack(Tile.Rough), y, x);
                }
                else
                {
                    masterSector.setStack(new TileStack(Tile.Smooth), y, x);
                }
            }
        }

        return masterSector;
    }
}

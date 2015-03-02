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

package com.andgate.ikou.model;

import com.andgate.ikou.maze.MazeGenerator;
import com.andgate.ikou.maze.RecursiveBacktrackerMazeGenerator;
import com.andgate.ikou.model.TileStack.Tile;
import com.andgate.ikou.Constants;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;
import java.util.Random;

public class Level
{
    public LinkedList<Floor> floors = new LinkedList<>();
    private Random random = new Random();

    public static final int BASE_SIZE = 3;

    MazeGenerator mazegen;

    public Level()
    {
        this(-1);
    }

    public Level(long seed)
    {
        if(seed != -1) random.setSeed(seed);

        for(int i = 0; i < 10; i++)
        {
            addRandomFloor();
        }
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        for(Floor floor : floors)
        {
            floor.render(modelBatch, environment);
        }
    }

    public void addRandomFloor()
    {
        addFloor(buildRandomFloor(random.nextLong()));
    }

    Vector2i tmpVec2i_1 = new Vector2i();
    Vector2i tmpVec2i_2 = new Vector2i();
    Vector2i tmpVec2i_3 = new Vector2i();

    public Floor buildRandomFloor(long mazeSeed)
    {
        int depth = floors.size() + 1;
        int width = BASE_SIZE * depth;
        int height = BASE_SIZE * depth;

        do
        {
            tmpVec2i_1.x = random.nextInt(width+1);
            tmpVec2i_1.y = random.nextInt(height+1);

            tmpVec2i_2.x = random.nextInt(width+1);
            tmpVec2i_2.y = random.nextInt(height+1);

            tmpVec2i_3.set(tmpVec2i_1);
            tmpVec2i_3.sub(tmpVec2i_2);
        } while(tmpVec2i_3.len() > (width + height) / 4);

        mazegen = new RecursiveBacktrackerMazeGenerator(width, height, tmpVec2i_1.x, tmpVec2i_1.y, tmpVec2i_1.x, tmpVec2i_1.y, mazeSeed);
        mazegen.generate();

        return mazegen.computeFloor();
    }

    public void addFloor(Floor floor)
    {
        floors.add(floor);
        offsetLastFloor();
    }

    private Vector2i offset = new Vector2i();

    private void offsetLastFloor()
    {
        if(floors.size() <= 1) return;

        Floor penult = floors.get(floors.size() - 2);
        Floor last = floors.getLast();

        Vector3i penultEnd = penult.getEnd();
        Vector3i lastStart = last.getStart();

        offset.add(penultEnd.x, penultEnd.z);
        offset.sub(lastStart.x, lastStart.z);

        float yOffset = (1-floors.size()) * Constants.FLOOR_SPACING;

        last.setOffset(offset.x, offset.y);
        last.setPosition(offset.x, yOffset, offset.y);
    }

    public Floor getFloor(int depth)
    {
        return floors.get(depth);
    }

    public Tile getTile(int floorNumber, int x, int y, int z)
    {
        return getFloor(floorNumber).getTile(x, y, z);
    }

    public TileStack getTileStack(int depth, int x,  int z)
    {
        return floors.get(depth).getTileStack(x, z);
    }

    private Vector3 startPosition = new Vector3();
    public Vector3 getStartPosition(int depth)
    {
        Floor floor = floors.get(depth);

        floor.getPosition(startPosition);

        Vector3i floorStart = floor.getStart();
        startPosition.add(floorStart.x, 0.0f, floorStart.z);

        startPosition.y = TileStack.HEIGHT - depth * Constants.FLOOR_SPACING;

        return startPosition;
    }

    public void shrink()
    {
        for(Floor floor : floors)
        {
            floor.shrink();
        }
    }

    public void dispose()
    {
        for(Floor floor : floors)
        {
            floor.dispose();
        }
    }
}

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

import com.andgate.ikou.Constants;
import com.andgate.ikou.maze.MazeGenerator;
import com.andgate.ikou.maze.RecursiveBacktrackerMazeGenerator;
import com.andgate.ikou.render.FloorRender;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.LinkedList;
import com.andgate.ikou.utility.XorRandomGen;

public class Level
{
    MazeGenerator mazegen;
    public LinkedList<Floor> floors = new LinkedList<>();

    private XorRandomGen random;
    private long seed;
    int min_size, max_size, floor_count;

    private PerspectiveCamera camera = null;

    public Level(long seed, int min_size, int max_size, int floor_count)
    {
        random = new XorRandomGen(seed);
        this.seed = seed;
        this.min_size = min_size;
        this.max_size = max_size;
        this.floor_count = floor_count;

        for(int i = 0; i < floor_count; i++)
        {
            addRandomFloor();
            floors.getLast().getRender().build();
        }
    }

    public long getSeed()
    {
        return seed;
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
        addFloor(buildRandomFloor(random.next()));
    }

    Vector2i tmpVec2i_1 = new Vector2i();
    Vector2i tmpVec2i_2 = new Vector2i();
    Vector2i tmpVec2i_3 = new Vector2i();

    public Floor buildRandomFloor(long mazeSeed)
    {
        int depth = floors.size() + depthOffset + 1;
        int width = random.nextInt(min_size, max_size);
        int height = random.nextInt(min_size, max_size);

        do
        {
            tmpVec2i_1.x = random.nextInt(0, width);
            tmpVec2i_1.y = random.nextInt(0, height);

            tmpVec2i_2.x = random.nextInt(0, width);
            tmpVec2i_2.y = random.nextInt(0, height);

            tmpVec2i_3.set(tmpVec2i_1);
            tmpVec2i_3.sub(tmpVec2i_2);
        } while(tmpVec2i_3.len() <= Math.sqrt(width * width + height * height) / 2);

        mazegen = new RecursiveBacktrackerMazeGenerator(width - 1, height - 1, tmpVec2i_1.x, tmpVec2i_1.y, tmpVec2i_2.x, tmpVec2i_2.y, mazeSeed);
        mazegen.generate();

        return mazegen.computeFloor();
    }

    private int depthOffset = 0;

    public void addFloor(Floor floor)
    {
        floor.getRender().setCamera(camera);
        floors.add(floor);
        offsetLastFloor();
    }

    private Vector2i offset = new Vector2i();
    private float yOffset = 0;

    private void offsetLastFloor()
    {
        int depth = (floors.size() - 1) + depthOffset;
        if(depth < 1) return;

        Floor penult = floors.get(floors.size() - 2);
        Floor last = floors.getLast();

        Vector3i penultEnd = penult.getEnd();
        Vector3i lastStart = last.getStart();

        tmpVec2i_1.set(penultEnd.x, penultEnd.z);
        tmpVec2i_1.sub(lastStart.x, lastStart.z);
        offset.add(tmpVec2i_1);

        float yOffset = -depth * Constants.FLOOR_SPACING;

        last.setOffset(offset.x, offset.y);
        last.setPosition(offset.x, yOffset, offset.y);
    }

    public Floor getFloor(int depth)
    {
        int index = getIndex(depth);
        return floors.get(index);
    }


    public TileStack getTileStack(int depth, int x,  int z)
    {
        int index = getIndex(depth);
        return floors.get(index).getTileStack(x, z);
    }

    private Vector3 startPosition = new Vector3();
    public Vector3 getStartPosition(int depth)
    {
        int index = getIndex(depth);
        Floor floor = floors.get(index);

        floor.getPosition(startPosition);

        Vector3i floorStart = floor.getStart();
        startPosition.add(floorStart.x, 0.0f, floorStart.z);

        startPosition.y = TileStack.HEIGHT - depth * Constants.FLOOR_SPACING;

        return startPosition;
    }

    public void setCamera(PerspectiveCamera camera)
    {
        this.camera = camera;

        for(Floor floor : floors)
        {
            FloorRender floorRender = floor.getRender();
            floorRender.setCamera(camera);
        }
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

    private int getIndex(int depth)
    {
        return depth - depthOffset;
    }
}

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
import com.andgate.ikou.render.FloorRender;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;
import java.util.Random;

public class Level
{
    public static final int BASE_SIZE = 3;
    public static final int VIEWABLE_FLOORS = 5;
    public static final int VIEWABLE_FLOORS_ADJECENT = 2;

    MazeGenerator mazegen;
    public LinkedList<Floor> floors = new LinkedList<>();

    private Random random = new Random();
    private long seed;

    private PerspectiveCamera camera = null;

    public Level()
    {
        this(Constants.RESERVED_SEED);
    }

    public Level(long seed)
    {
        setSeed(seed);

        for(int i = 0; i < VIEWABLE_FLOORS; i++)
        {
            addRandomFloor();
            floors.getLast().getRender().build();
        }
    }

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(long seed)
    {
        if(seed == Constants.RESERVED_SEED)
        {
            this.seed = random.nextLong();
            this.setSeed(this.seed);
        }
        else
        {
            this.seed = seed;
            random.setSeed(seed);
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
        int depth = floors.size() + depthOffset + 1;
        int length = BASE_SIZE * depth;

        do
        {
            tmpVec2i_1.x = random.nextInt(length);
            tmpVec2i_1.y = random.nextInt(length);

            tmpVec2i_2.x = random.nextInt(length);
            tmpVec2i_2.y = random.nextInt(length);

            tmpVec2i_3.set(tmpVec2i_1);
            tmpVec2i_3.sub(tmpVec2i_2);
        } while(tmpVec2i_3.len() <= (length / 2));

        mazegen = new RecursiveBacktrackerMazeGenerator(length - 1, length - 1, tmpVec2i_1.x, tmpVec2i_1.y, tmpVec2i_2.x, tmpVec2i_2.y, mazeSeed);
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

    public void startNextFloor(int playerDepth)
    {
        startNextFloor(buildRandomFloor(random.nextLong()), playerDepth);
    }

    public void startNextFloor(Floor newFloor, int playerDepth)
    {
        if(playerDepth >= VIEWABLE_FLOORS_ADJECENT)
        {
            floors.removeFirst().dispose();
            depthOffset++;
            addFloor(newFloor);
        }
    }


    public void startNextFloorThreaded(final int playerDepth)
    {
        if(playerDepth >= VIEWABLE_FLOORS_ADJECENT)
        {
            new FloorBuilderThread(seed, playerDepth).start();
        }
    }

    private class FloorBuilderThread extends Thread
    {
        private long seed;
        private int playerDepth;
        public FloorBuilderThread(long seed, int playerDepth)
        {
            this.seed = seed;
            this.playerDepth = playerDepth;
        }

        @Override
        public void run()
        {
            Floor floor = buildRandomFloor(playerDepth);

            Gdx.app.postRunnable(new FloorPoster(floor));
        }

        private class FloorPoster implements Runnable
        {
            Floor floor;

            public FloorPoster(Floor floor)
            {
                this.floor = floor;
            }

            @Override
            public void run()
            {
                // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                Level.this.startNextFloor(floor, playerDepth);
                floor.getRender().build();
            }
        }
    }

    public void initializePlayerDepth(int playerDepth)
    {
        for(int i = 0; i <= playerDepth; i++)
        {
            startNextFloor(i);
            floors.getLast().getRender().build();
        }
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

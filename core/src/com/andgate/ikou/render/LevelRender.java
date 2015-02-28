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

package com.andgate.ikou.render;

import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.utility.Vector2i;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class LevelRender implements RenderableProvider, Disposable
{
    private final Level level;
    private final FloorRender[] floorRenders;

    public LevelRender(Level level)
    {
        this.level = level;
        Floor[] floors = level.getFloors();
        floorRenders = new FloorRender[floors.length];

        for(int i = 0; i < floorRenders.length; i++)
        {
            floorRenders[i] = new FloorRender(floors[i]);
        }
    }

    public void updateFloors(float delta)
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.getTransformer().update(delta);
        }
    }

    public void offsetFloors()
    {
        for(int i = 1; i < floorRenders.length; i++)
        {
            Vector2i offset = level.getFloorOffset(i);
            floorRenders[i].getTransformer().translate(offset.x, 0.0f, offset.y);
        }
    }

    public void spaceFloors(float spacing)
    {
        for(int i = 1; i < floorRenders.length; i++)
        {
            floorRenders[i].getTransformer().translate(0.0f, -spacing * i, 0.0f);
        }
    }

    public void resetTransform()
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.getTransformer().reset();
        }
    }

    public void scaleFloorsToBoxSize(float length)
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.getTransformer().scaleToBoxSize(length);
        }
    }

    public void updateFloorTransformers()
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.getTransformer().apply();
        }
    }

    public FloorRender[] getFloorRenders()
    {
        return floorRenders;
    }

    public PerspectiveCamera getCamera()
    {
        if(floorRenders.length >= 1)
            return floorRenders[1].getCamera();

        return null;
    }

    public void setCamera(PerspectiveCamera camera)
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.setCamera(camera);
        }
    }

    public void centerOnOrigin()
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.getTransformer().centerOnOrigin();
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.getRenderables(renderables, pool);
        }
    }

    @Override
    public void dispose()
    {
        for(FloorRender floorRender : floorRenders)
        {
            floorRender.dispose();
        }
    }
}

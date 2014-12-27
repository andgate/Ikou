package com.andgate.ikou.render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.utility.Vector2i;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class LevelRender implements RenderableProvider, Disposable
{
    FloorRender[] renders;

    public LevelRender(Level level, PerspectiveCamera camera)
    {
        Floor[] floors = level.getFloors();
        renders = new FloorRender[floors.length];

        for(int i = 0; i < renders.length; i++)
        {
            renders[i] = new FloorRender(floors[i].getMasterSector(), camera);

            // x and z calculated from start and goal positions of maze and next maze, yea?
            Vector3 position = new Vector3();
            position.y -= i * Constants.FLOOR_SPACING;
            renders[i].setPosition(position);

            if(i > 0)
            {
                Vector2i offset = level.getFloorOffset(i);
                renders[i].transform.translate(offset.x, 0.0f, offset.y);
            }
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for(FloorRender render : renders)
        {
            render.getRenderables(renderables, pool);
        }
    }

    @Override
    public void dispose()
    {
        for(FloorRender render : renders)
        {
            render.dispose();
        }
    }
}

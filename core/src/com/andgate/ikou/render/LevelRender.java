package com.andgate.ikou.render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.TileMaze;
import com.andgate.ikou.utility.Vector2i;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class LevelRender implements RenderableProvider, Disposable
{
    TileMazeRender[] renders;

    public LevelRender(Level level, PerspectiveCamera camera)
    {
        TileMaze[] mazes = level.getMazes();
        renders = new TileMazeRender[mazes.length];

        for(int i = 0; i < renders.length; i++)
        {
            renders[i] = new TileMazeRender(mazes[i], camera);

            // x and z calculated from start and goal positions of maze and next maze, yea?
            Vector3 position = new Vector3();
            position.y -= i * Constants.FLOOR_SPACING;
            renders[i].setPosition(position);

            if(i > 0)
            {
                Vector2i offset = level.calculateFloorOffset(i+1);
                renders[i].transform.translate(offset.x, 0.0f, offset.y);
            }
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for(TileMazeRender render : renders)
        {
            render.getRenderables(renderables, pool);
        }
    }

    @Override
    public void dispose()
    {
        for(TileMazeRender render : renders)
        {
            render.dispose();
        }
    }
}

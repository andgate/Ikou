package com.andgate.ikou.render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.TileMaze;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
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

        Vector3 offset = new Vector3();
        Vector3 currStartPosition = new Vector3();
        Vector3 lastEndPosition = new Vector3();

        for(int i = 0; i < renders.length; i++)
        {
            renders[i] = new TileMazeRender(mazes[i], camera);

            // x and z calculated from start and goal positions of maze and next maze, yea?
            Vector3 position = new Vector3();
            position.y -= i * Constants.FLOOR_SPACING;
            renders[i].setPosition(position);

            if( (i-1) >= 0)
            {
                currStartPosition.x = mazes[i].getStartPosition().x;
                currStartPosition.z = mazes[i].getStartPosition().y;

                lastEndPosition.x = mazes[i-1].getEndPosition().x;
                lastEndPosition.z = mazes[i-1].getEndPosition().y;

                offset.add(lastEndPosition);
                offset.sub(currStartPosition);

                renders[i].transform.translate(offset);
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

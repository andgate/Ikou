package com.andgate.ikou.Render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Model.Level;
import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Tiles.TileData;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
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

        for(int i = 0; i < renders.length; i++)
        {
            renders[i] = new TileMazeRender(mazes[i], camera);
            Vector3 position = new Vector3();
            // x and z calculated from start and goal positions of maze and next maze, yea?
            position.y -= i * Constants.FLOOR_SPACING;
            renders[i].setPosition(position);
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

package com.andgate.ikou.Render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Tiles.TileSector;
import com.andgate.ikou.Tiles.TileStack;
import com.andgate.ikou.Tiles.TileData;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class TileWorldRender implements RenderableProvider, Disposable
{
    private Mesh mesh;
    public final Matrix4 transform = new Matrix4();

    public TileWorldRender(TileMaze maze)
    {
        // Build a master sector of the maze
        TileSector masterSector = new TileSector(maze);
        SectorMeshBuilder worldMeshBuilder = new SectorMeshBuilder(masterSector);
        mesh = worldMeshBuilder.build();
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        Renderable renderable = pool.obtain();
        renderable.material = TileData.TILE_MATERIAL;
        renderable.meshPartOffset = 0;
        renderable.meshPartSize = mesh.getNumIndices();
        renderable.primitiveType = GL20.GL_TRIANGLES;
        renderable.mesh = mesh;
        renderables.add(renderable);

        renderable.worldTransform.set(transform);
    }

    @Override
    public void dispose()
    {
        mesh.dispose();
    }
}

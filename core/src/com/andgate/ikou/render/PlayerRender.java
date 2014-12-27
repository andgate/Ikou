package com.andgate.ikou.render;

import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.model.tile.TileFactory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class PlayerRender implements RenderableProvider, Disposable
{
    private Mesh mesh;
    public final Matrix4 transform = new Matrix4();
    public final Material material;

    public PlayerRender()
    {
        // TODO make customizable
        TilePalette palette = new TilePalette();
        TileData playerTileData = TileFactory.build(TileData.TileType.Player, palette);

        TileMeshBuilder tileMeshBuilder = new TileMeshBuilder();
        tileMeshBuilder.addTile(playerTileData, 0, 0, 0);
        mesh = tileMeshBuilder.build();

        material = new Material(TileData.TILE_MATERIAL);
    }

    public void setColor(Color color)
    {
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        Renderable renderable = pool.obtain();
        renderable.material = material;
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

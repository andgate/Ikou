package com.andgate.ikou.render;

import com.andgate.ikou.model.TileMaze;
import com.andgate.ikou.model.TileSector;
import com.andgate.ikou.model.tile.TileData;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class TileMazeRender implements RenderableProvider, Disposable
{
    private static final int SUBSECTOR_SIZE = 8;
    private Mesh[][] meshes;
    public final Matrix4 transform = new Matrix4();
    private final PerspectiveCamera camera;

    public TileMazeRender(TileMaze maze, PerspectiveCamera camera)
    {
        this.camera = camera;
        // Build a master sector of the maze
        TileSector masterSector = new TileSector(maze);
        TileSector[][] subsectors = masterSector.split(0, 0, SUBSECTOR_SIZE);

        buildMeshes(subsectors);
    }

    private void buildMeshes(TileSector[][] subsectors)
    {
        int rows = subsectors.length;
        meshes = new Mesh[rows][];
        for(int currRow = 0; currRow < rows; currRow++)
        {
            int columns = subsectors[currRow].length;
            meshes[currRow] = new Mesh[columns];
            for(int currColumn = 0; currColumn < columns; currColumn++)
            {
                int offsetX = currColumn * SUBSECTOR_SIZE;
                int offsetZ = currRow * SUBSECTOR_SIZE;

                SectorMeshBuilder worldMeshBuilder
                        = new SectorMeshBuilder(subsectors[currRow][currColumn], offsetX, offsetZ);

                meshes[currRow][currColumn] = worldMeshBuilder.build();
            }
        }
    }

    public void setPosition(Vector3 position)
    {
        transform.idt().translate(position);
    }


    private final Vector3 subsectorPosition = new Vector3();

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for(int i = 0; i < meshes.length; i++)
        {
            for(int j = 0; j < meshes[i].length; j++)
            {
                transform.getTranslation(subsectorPosition);
                subsectorPosition.x += j*SUBSECTOR_SIZE;
                subsectorPosition.z += i*SUBSECTOR_SIZE;

                boolean inFrustum = camera.frustum.sphereInFrustum(subsectorPosition, SUBSECTOR_SIZE * 1.5f);

                if(inFrustum)
                {
                    Mesh mesh = meshes[i][j];

                    Renderable renderable = pool.obtain();
                    renderable.material = TileData.TILE_MATERIAL;
                    renderable.meshPartOffset = 0;
                    renderable.meshPartSize = mesh.getNumIndices();
                    renderable.primitiveType = GL20.GL_TRIANGLES;
                    renderable.mesh = mesh;
                    renderables.add(renderable);

                    renderable.worldTransform.set(transform);
                }
            }
        }
    }

    @Override
    public void dispose()
    {
        disposeMeshes();
    }

    public void disposeMeshes()
    {
        for(int i = 0; i < meshes.length; i++)
        {
            for(int j = 0; j < meshes.length; j++)
            {
                meshes[i][j].dispose();
            }
        }
    }
}

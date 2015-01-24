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
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileSector;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.utility.Array2d;
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

public class FloorRender implements RenderableProvider, Disposable
{
    private static final int SUBSECTOR_SIZE = TileSector.SIZE;
    private SectorMesh[][] sectorMeshes;
    public final Matrix4 floorTransform = new Matrix4();
    private final PerspectiveCamera camera;

    public FloorRender(Floor floor, PerspectiveCamera camera)
    {
        this.camera = camera;
        buildMeshes(floor);
    }

    private void buildMeshes(Floor floor)
    {
        Array2d<TileSector> sectors = floor.getMasterSector().getSectors();
        TilePalette palette = floor.getPalette();

        int rows = sectors.size;
        sectorMeshes = new SectorMesh[rows][];
        for(int currRow = 0; currRow < rows; currRow++)
        {
            int columns = sectors.get(currRow).size;
            sectorMeshes[currRow] = new SectorMesh[columns];
            for(int currColumn = 0; currColumn < columns; currColumn++)
            {
                int offsetX = currColumn * TileSector.SIZE;
                int offsetZ = currRow * TileSector.SIZE;

                SectorMesh sectorMesh
                        = new SectorMesh(floor.getMasterSector(), currRow, currColumn, palette, offsetX, offsetZ);

                sectorMesh.setNeedsRebuild();
                sectorMeshes[currRow][currColumn] = sectorMesh;
            }
        }
    }

    public void setPosition(Vector3 position)
    {
        floorTransform.idt().translate(position);
    }


    private final Vector3 subsectorPosition = new Vector3();

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for(int i = 0; i < sectorMeshes.length; i++)
        {
            for(int j = 0; j < sectorMeshes[i].length; j++)
            {
                Mesh mesh = sectorMeshes[i][j].getMesh();

                if(inFrustum(i, j) && (mesh != null))
                {
                    Renderable renderable = pool.obtain();
                    renderable.material = TileStack.TILE_MATERIAL;
                    renderable.meshPartOffset = 0;
                    renderable.meshPartSize = mesh.getNumIndices();
                    renderable.primitiveType = GL20.GL_TRIANGLES;
                    renderable.mesh = mesh;
                    renderables.add(renderable);

                    renderable.worldTransform.set(floorTransform);
                }
            }
        }
    }

    private boolean inFrustum(int sectorRow, int sectorColumn)
    {
        floorTransform.getTranslation(subsectorPosition);
        subsectorPosition.x += sectorColumn * SUBSECTOR_SIZE;
        subsectorPosition.z += sectorRow * SUBSECTOR_SIZE;

        return camera.frustum.sphereInFrustum(subsectorPosition, SUBSECTOR_SIZE * 1.5f);
    }

    @Override
    public void dispose()
    {
        disposeMeshes();
    }

    public void disposeMeshes()
    {
        for(int i = 0; i < sectorMeshes.length; i++)
        {
            for(int j = 0; j < sectorMeshes[i].length; j++)
            {
                SectorMesh sectorMesh = sectorMeshes[i][j];
                if(sectorMesh != null)
                    sectorMesh.dispose();
            }
        }
    }
}

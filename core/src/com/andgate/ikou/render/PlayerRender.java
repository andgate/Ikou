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

import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileStack;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class PlayerRender implements RenderableProvider, Disposable
{
    public final Material material;

    TileMesh tileMesh;

    public PlayerRender()
    {
        tileMesh = new TileMesh();
        tileMesh.addTile(Color.WHITE, 0, 0, 0);
        tileMesh.setNeedsRebuild();

        material = new Material(TileStack.TILE_MATERIAL);
    }

    public Matrix4 getTransform()
    {
        return tileMesh.getTransform();
    }

    public void setColor(Color color)
    {
        material.set(ColorAttribute.createDiffuse(color));
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        Renderable renderable = pool.obtain();
        renderable.material = material;
        renderable.meshPartOffset = 0;
        renderable.meshPartSize = tileMesh.getMesh().getNumIndices();
        renderable.primitiveType = GL20.GL_TRIANGLES;
        renderable.mesh = tileMesh.getMesh();
        renderables.add(renderable);

        renderable.worldTransform.set(tileMesh.getTransform());
    }

    @Override
    public void dispose()
    {
        if(tileMesh != null)
            tileMesh.dispose();
    }
}

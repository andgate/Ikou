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
import com.andgate.ikou.model.tile.TileData;
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
        TileData playerTileData = new TileData(TileStack.Tile.Player);

        TileMeshBuilder tileMeshBuilder = new TileMeshBuilder();
        tileMeshBuilder.addTile(palette.getColor(TileStack.Tile.Player), 0, 0, 0);
        mesh = tileMeshBuilder.build();

        material = new Material(TileStack.TILE_MATERIAL);
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

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

package com.andgate.ikou.graphics.player;

import com.andgate.ikou.constants.*
import com.andgate.ikou.graphics.util.CubeMesher
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Pool

class PlayerModel : RenderableProvider, Disposable
{
    val material = Material(TILE_MATERIAL)
    val mesh: Mesh
    val transform = Matrix4()

    init {
        val mesher = CubeMesher()
        mesher.calculateVerts(0f ,0f ,0f, TILE_SPAN, TILE_HEIGHT, TILE_SPAN)
        mesher.addAll(PLAYER_TILE_COLOR)
        mesh = mesher.build()
    }

    override fun getRenderables(renderables: Array<Renderable>, pool: Pool<Renderable>)
    {
        val renderable: Renderable = pool.obtain()
        renderable.material = material
        renderable.meshPart.offset = 0
        renderable.meshPart.size = mesh.getNumIndices()
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES
        renderable.meshPart.mesh = mesh
        renderables.add(renderable)

        renderable.worldTransform.set(transform)
    }

    override fun dispose()
    {
        mesh.dispose();
    }
}

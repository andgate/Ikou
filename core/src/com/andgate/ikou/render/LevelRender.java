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

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.utility.Vector2i;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class LevelRender implements RenderableProvider, Disposable
{
    FloorRender[] renders;

    public LevelRender(Level level, PerspectiveCamera camera)
    {
        Floor[] floors = level.getFloors();
        renders = new FloorRender[floors.length];

        for(int i = 0; i < renders.length; i++)
        {
            renders[i] = new FloorRender(floors[i], camera);

            // x and z calculated from start and goal positions of maze and next maze, yea?
            Vector3 position = new Vector3();
            position.y -= i * Constants.FLOOR_SPACING;
            renders[i].setPosition(position);

            if(i > 0)
            {
                Vector2i offset = level.getFloorOffset(i);
                renders[i].transform.translate(offset.x, 0.0f, offset.y);
            }
        }
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for(FloorRender render : renders)
        {
            render.getRenderables(renderables, pool);
        }
    }

    @Override
    public void dispose()
    {
        for(FloorRender render : renders)
        {
            render.dispose();
        }
    }
}

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

package com.andgate.ikou.View;

import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Render.TileWorldRender;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class TileMazeView implements Disposable
{
    private TileWorldRender tileWorldModel;

    public TileMazeView(TileMaze maze, Vector3 position, PerspectiveCamera camera)
    {
        tileWorldModel = new TileWorldRender(maze, camera);
        tileWorldModel.transform.setTranslation(position);
    }

    public TileWorldRender getModel()
    {
        return getModel();
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(tileWorldModel, environment);
    }

    @Override
    public void dispose()
    {
        tileWorldModel.dispose();
    }
}

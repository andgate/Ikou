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
import com.andgate.ikou.Render.TileMazeModelBuilder;
import com.andgate.ikou.TileCode;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class TileMazeView implements Disposable
{
    private Model tileMazeModel;
    private ModelInstance tileMazeModelInstance;

    public TileMazeView(TileMaze maze, Vector3 position)
    {
        tileMazeModel = TileMazeModelBuilder.build(maze);
        tileMazeModelInstance = new ModelInstance(tileMazeModel);
        tileMazeModelInstance.transform.setTranslation(position);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(tileMazeModelInstance, environment);
    }

    @Override
    public void dispose()
    {
        tileMazeModel.dispose();
    }
}

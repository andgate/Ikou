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
import com.andgate.ikou.maze.MazeGenerator;
import com.andgate.ikou.maze.RecursiveBacktrackerMazeGenerator;
import com.andgate.ikou.model.Floor;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Preview implements Disposable
{
    private ModelBatch modelBatch;
    private Floor floor;
    private PerspectiveCamera camera;
    private Environment environment;

    public Preview()
    {
        MazeGenerator mazeGenerator = new RecursiveBacktrackerMazeGenerator(100, 100, 40, 40, 50, 50);
        mazeGenerator.generate();
        floor = mazeGenerator.computeFloor();
        floor.getRender().build();

        modelBatch = new ModelBatch();
        setupCamera();
        setupEnvironment();
    }

    private void setupCamera()
    {
        camera = new PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Vector3i start = floor.getStart();
        Vector3i end = floor.getEnd();

        camera.position.set( start.x,
                start.y + Constants.CAMERA_VERTICAL_DISTANCE,
                start.z - Constants.CAMERA_HORIZONTAL_DISTANCE);

        camera.lookAt(end.x, end.y, end.z);
        camera.near = 1f;
        camera.far = Constants.CAMERA_FAR;
        camera.update();

        floor.getRender().setCamera(camera);
    }

    private void setupEnvironment()
    {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    public void render()
    {
        modelBatch.begin(camera);
        modelBatch.render(floor.getRender(), environment);
        modelBatch.end();
    }

    public void resize(int width, int height)
    {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update(true);
    }

    @Override
    public void dispose()
    {
        modelBatch.dispose();
        floor.dispose();
    }
}
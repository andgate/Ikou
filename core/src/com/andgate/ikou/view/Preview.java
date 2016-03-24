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

package com.andgate.ikou.view;

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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

public class Preview implements Disposable
{
    private ModelBatch modelBatch;
    private Floor groundFloor;
    private Floor upperFloor;
    private PerspectiveCamera camera;
    private Environment environment;

    public Preview()
    {
        int mazeLength = (int)Constants.CAMERA_FAR + 1;
        int mazeEnd = mazeLength / 2;
        int startToEndDistance = (int)Constants.CAMERA_FAR / 4;
        int mazeStartX = mazeEnd;
        int mazeStartZ = mazeEnd - startToEndDistance;

        Random rand = new Random();
        MazeGenerator mazeGenerator = new RecursiveBacktrackerMazeGenerator(mazeLength, mazeLength, mazeStartX, mazeStartZ, mazeEnd, mazeEnd, rand.nextLong());
        mazeGenerator.generate();
        groundFloor = mazeGenerator.computeFloor();
        groundFloor.getRender().build();

        mazeGenerator.setEnd(0,0);
        mazeGenerator.generate();
        upperFloor = mazeGenerator.computeFloor();
        upperFloor.getRender().build();
        upperFloor.getRender().transform.translate(0.0f, Constants.FLOOR_SPACING, 0.0f);

        modelBatch = new ModelBatch();
        setupCamera();
        createEnvironment();
    }

    private static final float PREVIEW_FIELD_OF_VIEW = 95f;

    private void setupCamera()
    {
        camera = new PerspectiveCamera(PREVIEW_FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Vector3i start = groundFloor.getStart();
        Vector3i end = groundFloor.getEnd();
        target.set(end.x, end.y, end.z);
        target.x += TileStack.HALF_WIDTH;
        target.z += TileStack.HALF_DEPTH;

        camera.position.set( start.x,
                start.y + Constants.CAMERA_VERTICAL_DISTANCE,
                start.z - Constants.CAMERA_HORIZONTAL_DISTANCE);

        camera.lookAt(target);
        camera.near = 1f;
        camera.far = Constants.CAMERA_FAR;
        camera.update();

        groundFloor.getRender().setCamera(camera);
        upperFloor.getRender().setCamera(camera);

        // Start with a 45 degree rotation
        rotate(45);
    }

    private void createEnvironment()
    {
        environment = new Environment();

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1f));

        Vector3 lightDirection1 = new Vector3(0, 0, 1.0f);
        lightDirection1.rot(new Matrix4().setFromEulerAngles(45.0f, 45.0f, 0.0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, lightDirection1));

        Vector3 lightDirection2 = new Vector3(0, 0, 1.0f);
        lightDirection2.rot(new Matrix4().setFromEulerAngles(15.0f, -15.0f, 0.0f));
        environment.add(new DirectionalLight().set(0.3f, 0.8f, 0.8f, lightDirection2));
    }

    public void render()
    {
        modelBatch.begin(camera);
        modelBatch.render(groundFloor.getRender(), environment);
        modelBatch.render(upperFloor.getRender(), environment);
        modelBatch.end();
    }

    private static final float ANGULAR_VELOCITY = 90f / 60f; // degrees per second

    public void update(float delta)
    {
        float xAngleDelta = ANGULAR_VELOCITY * delta;
        rotate(xAngleDelta);
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
        groundFloor.dispose();
    }

    private Vector3 tmpV1 = new Vector3();
    private Vector3 target = new Vector3();

    public void rotate(float xAngleDelta)
    {
        tmpV1.set(camera.direction).crs(camera.up).y = 0f;

        camera.rotateAround(target, Vector3.Y, xAngleDelta);
        camera.update();
    }
}
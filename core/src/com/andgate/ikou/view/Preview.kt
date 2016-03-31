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
import com.andgate.ikou.graphics.maze.MazeModel
import com.andgate.ikou.maze.Maze;
import com.andgate.ikou.maze.recursivebacktracker.RecursiveBacktrackerMaze;
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

class Preview : Disposable
{
    val batch = ModelBatch()
    var model: MazeModel

    private val PREVIEW_FIELD_OF_VIEW: Float = 95f
    private val camera = PerspectiveCamera(PREVIEW_FIELD_OF_VIEW, Gdx.graphics.getWidth().toFloat(), Gdx.graphics.getHeight().toFloat())
    private val environment = Environment()

    init
    {
        val mazeLength: Int = Constants.CAMERA_FAR.toInt() + 1
        val mazeFloors: Int = 2
        val mazeEnd: Int = mazeLength / 2
        val startToEndDistance: Int = Constants.CAMERA_FAR.toInt() / 4
        val mazeStartX: Int = mazeEnd
        val mazeStartZ: Int = mazeEnd - startToEndDistance

        val rand = Random()
        val maze = RecursiveBacktrackerMaze(mazeLength, mazeLength, mazeFloors, mazeFloors, rand.nextLong())

        setupCamera()
        model = MazeModel(maze.buildTileMap(), camera)
        createEnvironment()
    }

    private fun setupCamera()
    {
        /*
        val start: Vector3i = groundFloor.getStart()
        val end: Vector3i = groundFloor.getEnd()
        target.set(end.x, end.y, end.z)
        target.x += TileStack.HALF_WIDTH
        target.z += TileStack.HALF_DEPTH

        camera.position.set( start.x,
                start.y + Constants.CAMERA_VERTICAL_DISTANCE,
                start.z - Constants.CAMERA_HORIZONTAL_DISTANCE)

        camera.lookAt(target)
        camera.near = 1f
        camera.far = Constants.CAMERA_FAR
        camera.update()

        groundFloor.getRender().setCamera(camera)
        upperFloor.getRender().setCamera(camera)

        // Start with a 45 degree rotation
        rotate(45)
        */
    }

    private fun createEnvironment()
    {
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.set(ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1f))

        val lightDirection1 = Vector3(0f, 0f, 1f)
        lightDirection1.rot(Matrix4().setFromEulerAngles(45.0f, 45.0f, 0.0f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, lightDirection1))
    }

    fun render()
    {
        batch.begin(camera)
        batch.render(model, environment)
        batch.end()
    }

    private val ANGULAR_VELOCITY: Float = 90f / 60f // degrees per second
    fun update(delta_time: Float)
    {
        val xAngleDelta: Float = ANGULAR_VELOCITY * delta_time
        rotate(xAngleDelta)
    }

    fun resize(width: Int, height: Int)
    {
        camera.viewportWidth = width.toFloat()
        camera.viewportHeight = height.toFloat()
        camera.update(true)
    }

    override fun dispose()
    {
        model.dispose()
    }

    private val tmpV1: Vector3 = Vector3()
    private val target: Vector3 = Vector3()

    fun rotate(xAngleDelta: Float)
    {
        tmpV1.set(camera.direction).crs(camera.up).y = 0f
        camera.rotateAround(target, Vector3.Y, xAngleDelta)
        camera.update()
    }
}
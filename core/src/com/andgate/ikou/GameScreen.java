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

package com.andgate.ikou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter implements DirectionListener
{
    private static final String TAG = "GameScreen";

    private final Ikou game;
    private CameraInputController camController;

    private GameControlsMenu controlsMenu;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private Player player;
    private final Level level;
    private final LevelRender levelRender;

    private InputMultiplexer im;

    public GameScreen(Ikou game, Level level)
    {
        this.game = game;
        this.level = level;

        Gdx.graphics.setVSync(false);

        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, game.worldWidth, game.worldHeight);

        player = new Player(level);

        levelRender = new LevelRender(level, camera);

        createEnvironment();

        InputProcessor moveController = new PlayerDirectionGestureDetector(this, camera);
        im = new InputMultiplexer();
        im.addProcessor(moveController);
        Gdx.input.setInputProcessor(im);

        setupCamera();

        controlsMenu = new GameControlsMenu(game, im, moveController, camController);
    }

    private void setupCamera()
    {
        camera.position.set(player.getPosition().x,
                            player.getPosition().y + 3.0f,
                            player.getPosition().z - 3.0f);
        camera.lookAt(player.getPosition());
        camera.near = 1f;
        camera.far = 30f;
        camera.update();
        camController = new CameraInputController(camera);
    }

    private void createEnvironment()
    {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    @Override
    public void show()
    {}

    @Override
    public void render(float delta)
    {
        if(controlsMenu.currentMode == GameControlsMenu.Mode.MOVEMENT)
        {
            camera.position.set(player.getPosition());
            camera.position.x += TileData.WIDTH / 2.0f;
            camera.position.y += 3.0f;
            camera.position.z -= 3.0f;
            camera.lookAt(player.getPosition());
            camera.update();
        }
        else
        {
            camController.target = player.getPosition();
            camController.update();
        }

        renderSetup();

        modelBatch.begin(camera);
            modelBatch.render(levelRender, environment);
            player.render(modelBatch, environment);
        modelBatch.end();

        controlsMenu.render();

        doPhysicsStep(delta);
        controlsMenu.update();


        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }

        Gdx.app.debug(TAG, "FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    private float accumulator = 0.0f;

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            update(deltaTime);
            accumulator -= Constants.TIME_STEP;
        }
    }

    private void update(float delta)
    {
        player.update(delta);
    }

    private void renderSetup()
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void hide()
    {}

    @Override
    public void dispose()
    {
        levelRender.dispose();
        player.dispose();
        modelBatch.dispose();
        controlsMenu.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        camera.viewportHeight = game.worldHeight;
        camera.viewportWidth = game.worldWidth;
        camera.update(true);

        controlsMenu.build();
    }

    public void movePlayer(TileMaze.Direction velocity)
    {
        if(!player.isMoving())
        {
            TileMaze maze = level.getCurrentTileMaze();
            maze.move(velocity);

            Vector3 nextPosition = new Vector3();
            nextPosition.x = maze.getPlayerPosition().x;
            nextPosition.y = player.getPosition().y;
            nextPosition.z = maze.getPlayerPosition().y;

            player.moveTo(nextPosition);
        }
    }

    @Override
    public void onLeft()
    {
        movePlayer(TileMaze.Direction.Left);
    }

    @Override
    public void onRight()
    {
        movePlayer(TileMaze.Direction.Right);
    }

    @Override
    public void onUp()
    {
        movePlayer(TileMaze.Direction.Up);
    }

    @Override
    public void onDown()
    {
        movePlayer(TileMaze.Direction.Down);
    }
}

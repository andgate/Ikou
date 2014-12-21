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
import com.andgate.ikou.Ikou;
import com.andgate.ikou.controller.CameraInputController;
import com.andgate.ikou.controller.DirectionListener;
import com.andgate.ikou.controller.GameControlsMenu;
import com.andgate.ikou.controller.PlayerDirectionGestureDetector;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.TileMaze;
import com.andgate.ikou.render.LevelRender;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.utility.Vector2i;
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
        camera = new PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, game.worldWidth, game.worldHeight);

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
        float playerCenterX = player.getPosition().x + TileData.HALF_WIDTH;
        float playerCenterZ = player.getPosition().z + TileData.HALF_DEPTH;
        camera.position.set(playerCenterX,
                            player.getPosition().y + Constants.CAMERA_HEIGHT,
                            playerCenterZ - Constants.CAMERA_DISTANCE);
        camera.lookAt(playerCenterX, player.getPosition().y, playerCenterZ);
        camera.near = 1f;
        camera.far = Constants.CAMERA_FAR;
        camera.update();

        camController = new CameraInputController(camera, player);
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
        camController.update(delta);

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

        //Gdx.app.debug(TAG, "FPS: " + Gdx.graphics.getFramesPerSecond());
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

    public void movePlayer(Vector2i velocity)
    {
        if(!player.isMoving() && !player.isFalling())
        {
            TileMaze maze = level.getCurrentTileMaze();
            maze.move(velocity);
        }
    }

    @Override
    public void onLeft()
    {
        movePlayer(TileMaze.LEFT);
    }

    @Override
    public void onRight()
    {
        movePlayer(TileMaze.RIGHT);
    }

    @Override
    public void onUp()
    {
        movePlayer(TileMaze.UP);
    }

    @Override
    public void onDown()
    {
        movePlayer(TileMaze.DOWN);
    }
}

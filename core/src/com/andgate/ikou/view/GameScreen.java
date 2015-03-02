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
import com.andgate.ikou.controller.GameControlsMenu;
import com.andgate.ikou.controller.PlayerDirectionGestureDetector;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.Player;
import com.andgate.ikou.model.TilePalette;
import com.andgate.ikou.model.TileStack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;

import java.util.Random;

public class GameScreen extends ScreenAdapter
{
    private static final String TAG = "GameScreen";

    private enum GameMode
    {
        Play, Pause, Victory
    }

    private GameMode gameMode = GameMode.Play;

    private final Ikou game;
    private CameraInputController camController;

    private GameControlsMenu controlsMenu;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private final Level level;
    private final Player player;

    private ModelBatch shadowBatch;
    private DirectionalShadowLight shadowLight;

    private InputMultiplexer im;

    private SpriteBatch batch;

    public GameScreen(Ikou game)
    {
        this(game, -1);
    }

    public GameScreen(Ikou game, long seed)
    {
        this.game = game;
        batch = new SpriteBatch();

        if(seed != -1) level = new Level(seed); // use a seed
        else level = new Level(); // be completely random

        TilePalette palette = level.getFloor(1).getPalette();
        Color bg = palette.background;

        game.bloom.setClearColor(bg.r, bg.g, bg.b, bg.a);

        Gdx.graphics.setVSync(false);

        camera = new PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        modelBatch = new ModelBatch();

        // Start player on the first floor for now.
        // TODO: Start player at a depth reached before.
        player = new Player(game, level, 0);
        createEnvironment();

        setupCamera();
        InputProcessor moveController = new PlayerDirectionGestureDetector(player, camController);


        im = new InputMultiplexer();
        im.addProcessor(moveController);
        Gdx.input.setInputProcessor(im);
        controlsMenu = new GameControlsMenu(game, im, moveController, camController);
    }

    private void setupCamera()
    {
        float playerCenterX = player.getPosition().x + TileStack.HALF_WIDTH;
        float playerCenterZ = player.getPosition().z + TileStack.HALF_DEPTH;
        camera.position.set(playerCenterX,
                            player.getPosition().y + Constants.CAMERA_VERTICAL_DISTANCE,
                            playerCenterZ - Constants.CAMERA_HORIZONTAL_DISTANCE);
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

        shadowLight = new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f);
        shadowLight.set(0.8f, 0.8f, 0.8f, -1f, -.8f, -.2f);
        //environment.shadowMap = shadowLight;

        shadowBatch = new ModelBatch(new DepthShaderProvider());
    }

    @Override
    public void show()
    {}


    @Override
    public void render(float delta)
    {
        camController.update(delta);

        renderScene();
        renderOverlay();
        update(delta);
    }

    private void update(float delta)
    {
        switch(gameMode)
        {
            case Play:
                updatePlay(delta);
                break;
            case Pause:
                updatePause(delta);
                break;
            case Victory:
                updateVictory(delta);
                break;
        }
    }

    private void updatePlay(float delta)
    {
        doPhysicsStep(delta);
        controlsMenu.update();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }
    }

    private void updatePause(float delta)
    {

    }

    private void updateVictory(float delta)
    {

    }

    private void renderScene()
    {
        renderSetup();

        /*shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(playerRender);
        shadowBatch.end();
        shadowLight.end();*/

        game.bloom.capture();

        modelBatch.begin(camera);
        level.render(modelBatch, environment);
        modelBatch.render(player.getRender(), environment);
        modelBatch.end();

        game.bloom.render();
    }

    private void renderOverlay()
    {
        controlsMenu.render();

        String fpsString = "FPS: " + Gdx.graphics.getFramesPerSecond();
        float font_height = game.menuOptionFont.getCapHeight() * game.menuOptionFont.getScale();
        float font_y = Gdx.graphics.getHeight() - font_height;

        batch.begin();
        batch.setShader(game.fontShader);
        game.menuOptionFont.setColor(Color.BLACK);
        game.menuOptionFont.draw(batch, fpsString, game.ppm, font_y);
        batch.setShader(null);
        batch.end();
    }

    private float accumulator = 0.0f;

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            player.update(deltaTime);
            accumulator -= Constants.TIME_STEP;
        }
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
        level.dispose();
        modelBatch.dispose();
        controlsMenu.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

        camera.viewportHeight = game.worldHeight;
        camera.viewportWidth = game.worldWidth;
        camera.update(true);

        controlsMenu.resize(width, height);
    }
}

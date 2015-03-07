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
import com.andgate.ikou.input.CameraGestureDetector;
import com.andgate.ikou.input.GameControlsMenu;
import com.andgate.ikou.input.PlayerControllerListener;
import com.andgate.ikou.input.PlayerGestureDetector;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.Player;
import com.andgate.ikou.render.ThirdPersonCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter
{
    private static final String TAG = "GameScreen";

    private final Ikou game;
    private CameraGestureDetector cameraGestureDetector;

    private ThirdPersonCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private Level level;
    private Player player;

    private InputMultiplexer im;

    private SpriteBatch batch;

    private FrameBuffer textFBO;
    private Sprite textSprite;

    private PlayerControllerListener playerControllerListener;
    //private CameraControllerListener cameraControllerListener;

    public GameScreen(Ikou game, boolean isNewGame)
    {
        this.game = game;
        batch = new SpriteBatch();

        if(isNewGame)
        {
            startNewGame();
        }
        else
        {
            loadLastGame();
        }

        Color bg = Constants.BACKGROUND_COLOR;
        game.bloom.setClearColor(bg.r, bg.g, bg.b, bg.a);

        modelBatch = new ModelBatch();
        createEnvironment();

        camera = new ThirdPersonCamera(player);

        level.setCamera(camera);

        InputProcessor playerInputProcessor = new PlayerGestureDetector(player, camera);
        InputProcessor cameraInputProcessor = new CameraGestureDetector(camera);
        im = new InputMultiplexer();
        im.addProcessor(playerInputProcessor);
        im.addProcessor(cameraInputProcessor);
        Gdx.input.setInputProcessor(im);

        playerControllerListener = new PlayerControllerListener(player, camera);
        Controllers.addListener(playerControllerListener);

        buildTextLayer();

        Gdx.input.setCursorCatched(true);
    }

    private void buildTextLayer()
    {
        textFBO = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        TextureRegion textTextureRegion = new TextureRegion(textFBO.getColorBufferTexture());
        textTextureRegion.flip(false, true);
        textSprite = new Sprite(textTextureRegion);

    }

    private void startNewGame()
    {
        level = new Level(); // be completely random
        player = new Player(game, level, Constants.DEFAULT_DEPTH);
        player.saveProgress();
    }

    private void loadLastGame()
    {
        Preferences prefs = Gdx.app.getPreferences(Constants.PLAYER_PREFS);

        long seed = prefs.getLong(Constants.PLAYER_PREF_LEVEL_SEED, Constants.RESERVED_SEED);
        level = new Level(seed);

        int depth = prefs.getInteger(Constants.PLAYER_PREF_DEPTH, Constants.DEFAULT_DEPTH);
        level.initializePlayerDepth(depth);
        player = new Player(game, level, depth);

        final Vector3 defaultStart = level.getStartPosition(depth);
        float playerX = prefs.getFloat(Constants.PLAYER_PREF_X, defaultStart.x);
        float playerY = prefs.getFloat(Constants.PLAYER_PREF_Y, defaultStart.y);
        float playerZ = prefs.getFloat(Constants.PLAYER_PREF_Z, defaultStart.z);

        player.setPosition(playerX, playerY, playerZ);
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
        renderScene();
        if(game.debug) renderOverlay();

        update(delta);
    }

    private void update(float delta)
    {
        updatePlay(delta);
    }

    private void updatePlay(float delta)
    {
        doPhysicsStep(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }
    }

    private void renderScene()
    {
        renderSetup();

        //game.bloom.capture();
            modelBatch.begin(camera);
                level.render(modelBatch, environment);
                modelBatch.render(player.getRender(), environment);
            modelBatch.end();
        //game.bloom.render();
    }

    private void renderOverlay()
    {
        String fpsString = "FPS: " + Gdx.graphics.getFramesPerSecond() + ",  Seed: " + level.getSeed();
        float font_height = game.menuOptionFont.getCapHeight() * game.menuOptionFont.getScale();
        float font_y = Gdx.graphics.getHeight() - font_height;

        textFBO.begin();
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            batch.begin();
            batch.setShader(game.fontShader);
                game.menuOptionFont.setColor(Color.BLACK);
                game.menuOptionFont.draw(batch, fpsString, game.ppm, font_y);
            batch.setShader(null);
            batch.end();
        textFBO.end();

        batch.begin();
        textSprite.draw(batch);
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


        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void hide()
    {}

    @Override
    public void dispose()
    {
        Controllers.removeListener(playerControllerListener);
        level.dispose();
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

        camera.resize(width, height);

        buildTextLayer();
    }
}

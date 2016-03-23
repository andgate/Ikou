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
import com.andgate.ikou.input.CameraControllerListener;
import com.andgate.ikou.input.CameraGestureDetector;
import com.andgate.ikou.input.GameScreenControllerListener;
import com.andgate.ikou.input.GameScreenInputListener;
import com.andgate.ikou.input.PlayerControllerListener;
import com.andgate.ikou.input.PlayerGestureDetector;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.Player;
import com.andgate.ikou.render.ThirdPersonCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.phoenixframework.channels.*;

public class GameScreen extends ScreenAdapter
{
    private static final String TAG = "GameScreen";

    private final Ikou game;

    private Level level;
    private Player player;

    private ThirdPersonCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private SpriteBatch batch;

    private InputMultiplexer im;

    private GameScreenControllerListener gameScreenControllerListener;
    private PlayerControllerListener playerControllerListener;
    private CameraControllerListener cameraControllerListener;

    private enum State
    { Start, Play, End }
    private State state = State.Start;

    Socket socket;
    Channel channel;

    public GameScreen(Ikou game, boolean isNewGame)
    {
        // Test code for phoenix websocket
        try {
            socket = new Socket("ws://phoenix-andgate1.c9users.io:8080/socket/websocket");
            socket.connect();

            channel = socket.chan("rooms:lobby", null);

            channel.join()
                    .receive("ignore", new IMessageCallback() {
                        @Override
                        public void onMessage(Envelope envelope) {
                            Gdx.app.log("WebSocket", "IGNORE");
                        }
                    })
                    .receive("ok", new IMessageCallback() {
                        @Override
                        public void onMessage(Envelope envelope) {
                            Gdx.app.log("WebSocket", "JOINED with " + envelope.toString());
                        }
                    });

            channel.on("new:msg", new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    Gdx.app.log("WebSocket", "NEW MESSAGE: " + envelope.toString());
                }
            });

            channel.onClose(new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    Gdx.app.log("WebSocket", "CLOSED: " + envelope.toString());
                }
            });

            //Sending a message. This library uses Jackson for JSON serialization
            ObjectNode node = new ObjectNode(JsonNodeFactory.instance)
                    .put("user", "my_username")
                    .put("body", "hello from ikou!");

            channel.push("new:msg", node);
        } catch(Exception e) {
            Gdx.app.error("WebSocket", "Failed to connect", e);
        }

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

        modelBatch = new ModelBatch();
        createEnvironment();

        camera = new ThirdPersonCamera(player);

        level.setCamera(camera);

        InputProcessor gameScreenInputProcessor = new GameScreenInputListener(this);
        InputProcessor playerInputProcessor = new PlayerGestureDetector(player, camera);
        InputProcessor cameraInputProcessor = new CameraGestureDetector(camera);
        im = new InputMultiplexer();
        im.addProcessor(gameScreenInputProcessor);
        im.addProcessor(playerInputProcessor);
        im.addProcessor(cameraInputProcessor);
        Gdx.input.setInputProcessor(im);

        gameScreenControllerListener = new GameScreenControllerListener(this);
        playerControllerListener = new PlayerControllerListener(player, camera);
        cameraControllerListener = new CameraControllerListener(camera);
        Controllers.addListener(gameScreenControllerListener);
        Controllers.addListener(playerControllerListener);
        Controllers.addListener(cameraControllerListener);

        Gdx.input.setCursorCatched(true);
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
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 1f));

        Vector3 lightDirection1 = new Vector3(0, 0, 1.0f);
        lightDirection1.rot(new Matrix4().setFromEulerAngles(45.0f, 45.0f, 0.0f));
        environment.add(new DirectionalLight().set(0.3f, 0.3f, 0.3f, lightDirection1));

        /*Vector3 lightDirection2 = new Vector3(0, 0, 1.0f);
        lightDirection2.rot(new Matrix4().setFromEulerAngles(15.0f, -15.0f, 0.0f));
        environment.add(new DirectionalLight().set(0.3f, 0.8f, 0.8f, lightDirection2));*/
    }

    @Override
    public void show()
    {}


    @Override
    public void render(float delta)
    {
        playerControllerListener.update(delta);
        cameraControllerListener.update(delta);

        renderScene();
        //renderDepthInfo();
        //if(game.debug) renderDebugInfo();

        update(delta);
    }

    private void update(float delta)
    {
        switch(state)
        {
            case Start:
                updateStart(delta);
                break;
            case Play:
                updatePlay(delta);
                break;
            case End:
                updateEnd(delta);
                break;
            default:
                break;
        }
    }

    private void updateStart(float delta)
    {
        state = State.Play;
    }

    private void updatePlay(float delta)
    {
        doPhysicsStep(delta);
    }

    private void updateEnd(float delta)
    {
        game.setScreen(new MainMenuScreen(game));
        this.dispose();
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

    /*private void renderDepthInfo()
    {
        final String fpsString = "" + (player.getDepth() + 1);
        final float font_height = game.menuOptionFont.getLineHeight() * game.menuOptionFont.getScale();
        //final float font_y = Gdx.graphics.getHeight() - font_height;

        BitmapFont.TextBounds bounds = game.menuOptionFont.getBounds(fpsString);

        final float font_y = Gdx.graphics.getHeight();
        final float font_x = (Gdx.graphics.getWidth() - bounds.width) / 2.0f;

        final float overlaybarWidth = Gdx.graphics.getWidth();
        final float overlaybarHeight = (bounds.height * 4f / 3f);
        final float overlaybarX = 0.0f;
        final float overlaybarY = Gdx.graphics.getHeight() - overlaybarHeight;

        batch.begin();
        game.whiteTransparentOverlay.draw(batch, overlaybarX, overlaybarY, overlaybarWidth, overlaybarHeight);
        batch.setShader(game.fontShader);
        game.menuOptionFont.setColor(Color.BLACK);
        game.menuOptionFont.draw(batch, fpsString, font_x, font_y);
        batch.setShader(null);
        batch.end();

    }

    private void renderDebugInfo()
    {
        final String fpsString = "FPS: " + Gdx.graphics.getFramesPerSecond() + "\nSeed: " + level.getSeed();
        final float font_height = game.menuOptionFont.getLineHeight() * game.menuOptionFont.getScale();
        final float lineNumbers = 2.0f;
        final float font_y = font_height * lineNumbers;

        batch.begin();
        batch.setShader(game.fontShader);
            game.menuOptionFont.setColor(Color.BLACK);
            game.menuOptionFont.drawMultiLine(batch, fpsString, game.ppm, font_y);
        batch.setShader(null);
        batch.end();

    }*/

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
        Controllers.removeListener(gameScreenControllerListener);
        Controllers.removeListener(cameraControllerListener);
        Controllers.removeListener(playerControllerListener);
        level.dispose();
        modelBatch.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

        camera.resize(width, height);
    }

    public void end()
    {
        state = State.End;
    }
}

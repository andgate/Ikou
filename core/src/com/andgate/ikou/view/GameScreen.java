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
import com.andgate.ikou.actor.MazeActor;
import com.andgate.ikou.actor.PlayerActor;
import com.andgate.ikou.input.CameraControllerListener;
import com.andgate.ikou.input.CameraGestureDetector;
import com.andgate.ikou.input.GameScreenControllerListener;
import com.andgate.ikou.input.GameScreenInputListener;
import com.andgate.ikou.input.PlayerControllerListener;
import com.andgate.ikou.input.PlayerGestureDetector;
import com.andgate.ikou.input.PlayerGestureDetector.DirectionGestureListener;
import com.andgate.ikou.graphics.camera.ThirdPersonCamera;
import com.andgate.ikou.ui.SinglePlayerUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import org.phoenixframework.channels.*;

public class GameScreen extends ScreenAdapter
{
    private static final String TAG = "GameScreen";

    private final Ikou game;

    private SinglePlayerUI ui;

    private MazeActor maze;
    private PlayerActor player;

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
        /*try {
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
        }*/

        this.game = game;
        batch = new SpriteBatch();

        player = new PlayerActor(this);
        maze = new MazeActor(this, tiles, new PlayerActor[]{player});

        Color bg = Constants.BACKGROUND_COLOR;

        modelBatch = new ModelBatch();
        createEnvironment();

        camera = new ThirdPersonCamera(player);

        level.setCamera(camera);

        InputProcessor gameScreenInputProcessor = new GameScreenInputListener(this);
        InputProcessor playerInputProcessor = new PlayerGestureDetector(new DirectionGestureListener(maze, currPlayerId, camera));
        InputProcessor cameraInputProcessor = new CameraGestureDetector(camera);
        im = new InputMultiplexer();
        im.addProcessor(gameScreenInputProcessor);
        im.addProcessor(playerInputProcessor);
        im.addProcessor(cameraInputProcessor);
        Gdx.input.setInputProcessor(im);

        gameScreenControllerListener = new GameScreenControllerListener(this);
        playerControllerListener = new PlayerControllerListener(maze, currPlayerId, camera);
        cameraControllerListener = new CameraControllerListener(camera);
        Controllers.addListener(gameScreenControllerListener);
        Controllers.addListener(playerControllerListener);
        Controllers.addListener(cameraControllerListener);

        Gdx.input.setCursorCatched(true);

        ui = new SinglePlayerUI(this.game, level, player);
        ui.build();
    }

    /*private void startNewGame()
    {
        level = new Level(65487654, 5, 10, 5); // be completely random
        player = new Player(game, level, Constants.DEFAULT_DEPTH);
        player.saveProgress();
    }

    private void loadLastGame()
    {
        Preferences prefs = Gdx.app.getPreferences(Constants.PLAYER_PREFS);

        long seed = prefs.getLong(Constants.PLAYER_PREF_LEVEL_SEED, Constants.RESERVED_SEED);
        level = new Level(seed, 5, 10, 5);

        int depth = prefs.getInteger(Constants.PLAYER_PREF_DEPTH, Constants.DEFAULT_DEPTH);
        player = new Player(game, level, depth);

        final Vector3 defaultStart = level.getStartPosition(depth);
        float playerX = prefs.getFloat(Constants.PLAYER_PREF_X, defaultStart.x);
        float playerY = prefs.getFloat(Constants.PLAYER_PREF_Y, defaultStart.y);
        float playerZ = prefs.getFloat(Constants.PLAYER_PREF_Z, defaultStart.z);

        player.setPosition(playerX, playerY, playerZ);
    }*/

    private void createEnvironment()
    {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 0.7f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 0.7f));

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
        ui.update();

        renderScene();
        ui.stage.draw();

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

        modelBatch.begin(camera);
            level.render(modelBatch, environment);
            modelBatch.render(player.getRender(), environment);
        modelBatch.end();
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
        Controllers.removeListener(gameScreenControllerListener);
        Controllers.removeListener(cameraControllerListener);
        Controllers.removeListener(playerControllerListener);
        level.dispose();
        modelBatch.dispose();
        ui.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

        camera.resize(width, height);

        ui.build();
    }

    public void end()
    {
        state = State.End;
    }
}

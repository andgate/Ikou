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
import com.andgate.ikou.controller.PlayerDirectionGestureDetector.DirectionListener;
import com.andgate.ikou.io.ProgressDatabaseService;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.ProgressDatabase;
import com.andgate.ikou.model.TileMazeSimulator;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.render.LevelRender;
import com.andgate.ikou.render.PlayerRender;
import com.andgate.ikou.utility.Vector3i;
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
import com.badlogic.gdx.math.Vector2;

public class GameScreen extends ScreenAdapter implements DirectionListener
{
    private static final String TAG = "GameScreen";

    private final Ikou game;
    private CameraInputController camController;

    private GameControlsMenu controlsMenu;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private final Level level;
    private final LevelRender levelRender;
    private final PlayerTransformer playerTransformer;
    private final PlayerRender playerRender;

    private int currentFloor;

    private InputMultiplexer im;

    private SpriteBatch batch;

    private TileMazeSimulator mazeSim;

    public GameScreen(Ikou game, Level level, int startingFloor)
    {
        this.game = game;
        this.level = level;
        batch = new SpriteBatch();

        this.currentFloor = startingFloor;

        Gdx.graphics.setVSync(false);

        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, game.worldWidth, game.worldHeight);
        levelRender = new LevelRender(level, camera);

        playerTransformer = new PlayerTransformer(level.getStartPosition(currentFloor - 1));

        playerRender = new PlayerRender();
        playerRender.transform.set(playerTransformer.transform);

        mazeSim = new TileMazeSimulator(level.getFloor(currentFloor));

        createEnvironment();

        setupCamera();
        InputProcessor moveController = new PlayerDirectionGestureDetector(this, camController);


        im = new InputMultiplexer();
        im.addProcessor(moveController);
        Gdx.input.setInputProcessor(im);
        controlsMenu = new GameControlsMenu(game, im, moveController, camController);
    }

    private void setupCamera()
    {
        float playerCenterX = playerTransformer.getPosition().x + TileStack.HALF_WIDTH;
        float playerCenterZ = playerTransformer.getPosition().z + TileStack.HALF_DEPTH;
        camera.position.set(playerCenterX,
                            playerTransformer.getPosition().y + Constants.CAMERA_VERTICAL_DISTANCE,
                            playerCenterZ - Constants.CAMERA_HORIZONTAL_DISTANCE);
        camera.lookAt(playerCenterX, playerTransformer.getPosition().y, playerCenterZ);
        camera.near = 1f;
        camera.far = Constants.CAMERA_FAR;
        camera.update();

        camController = new CameraInputController(camera, playerTransformer);
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

        if(mazeSim.hasWon())
        {
            gotoNextFloor();
        }

        renderSetup();

        modelBatch.begin(camera);
            modelBatch.render(levelRender, environment);
            modelBatch.render(playerRender, environment);
        modelBatch.end();

        controlsMenu.render();

        doPhysicsStep(delta);
        controlsMenu.update();


        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
        }

        batch.begin();
        batch.setShader(game.fontShader);
        String fpsString = "FPS: " + Gdx.graphics.getFramesPerSecond();
        float fontHeight = game.menuOptionFont.getCapHeight() / game.ppm;
        game.menuOptionFont.setColor(Color.BLACK);
        game.menuOptionFont.draw(batch, fpsString, game.ppm, Gdx.graphics.getHeight() - fontHeight);
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
            update(deltaTime);
            accumulator -= Constants.TIME_STEP;
        }
    }

    private void update(float delta)
    {
        playerTransformer.update(delta);
        playerRender.transform.idt().translate(playerTransformer.getPosition());
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
        playerRender.dispose();
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


    Vector3i tmpDirection = new Vector3i();
    Vector3i displacement = new Vector3i();
    @Override
    public void moveInDirection(Vector2 direction)
    {
        if(!playerTransformer.isMoving() && !playerTransformer.isFalling())
        {
            tmpDirection.set(direction.x, 0.0f, direction.y);
            Vector3i displacement = mazeSim.move(tmpDirection);

            playerTransformer.moveBy(displacement.x, displacement.z);
        }
    }

    public void gotoNextFloor()
    {
        saveProgress();
        currentFloor++;
        playerTransformer.gotoNextLevel();

        mazeSim.setFloor(level.getFloor(currentFloor));
    }

    private void saveProgress()
    {
        ProgressDatabase progressDB = ProgressDatabaseService.read();
        int completedFloors = progressDB.getFloorsCompleted(level.getName());
        if(currentFloor > completedFloors)
        {
            progressDB.setFloorsCompleted(level.getName(), currentFloor);
            ProgressDatabaseService.write(progressDB);
        }
    }
}

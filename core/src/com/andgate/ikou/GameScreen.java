package com.andgate.ikou;

import com.andgate.ikou.Model.TileMaze;
import com.andgate.ikou.Utility.TileMazeParser;
import com.andgate.ikou.View.Player;
import com.andgate.ikou.View.Tile;
import com.andgate.ikou.View.TileMazeView;
import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter implements DirectionListener
{
    private final Ikou game;
    private CameraInputController camController;

    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private Player player;
    private TileMaze maze;
    private TileMazeView mazeView;

    private InputMultiplexer im;

    public GameScreen(Ikou game)
            throws InvalidFileFormatException
    {
        this.game = game;

        createEnvironment();
        modelBatch = new ModelBatch();

        FileHandle file = Gdx.files.internal("data/level/1.txt");
        maze = TileMazeParser.parse(file.readString());
        mazeView = new TileMazeView(maze);

        Vector3 playerStartPosition = new Vector3();
        playerStartPosition.x = maze.getInitialPlayerPosition().x;
        playerStartPosition.y = Tile.HEIGHT; // Above the floor
        playerStartPosition.z = maze.getInitialPlayerPosition().y;

        player = new Player(playerStartPosition);

        createCamera();

        im = new InputMultiplexer();
        im.addProcessor(new PlayerDirectionGestureDetector(this));
        //im.addProcessor(camController);
        Gdx.input.setInputProcessor(im);
    }

    private void createCamera()
    {
        camera = new PerspectiveCamera(67, game.worldWidth, game.worldHeight);
        camera.position.set(player.getPosition().x,
                            player.getPosition().y + 3.0f,
                            player.getPosition().z - 3.0f);
        //camera.position.set(0.0f, 3.0f, -3.0f);
        camera.lookAt(player.getPosition());
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
        camController = new CameraInputController(camera);
    }

    private void createEnvironment()
    {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -0.8f, 0.2f));
    }

    @Override
    public void show()
    {}

    @Override
    public void render(float delta)
    {
        camera.position.set(player.getPosition());
        camera.position.y += 3.0f;
        camera.position.z -= 3.0f;
        camera.lookAt(player.getPosition().x, 0.1f, player.getPosition().z);
        camera.update();
        camController.update();

        renderSetup();
        modelBatch.begin(camera);
            mazeView.render(modelBatch, environment);
            player.render(modelBatch, environment);
        modelBatch.end();

        doPhysicsStep(delta);
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

        Gdx.gl.glCullFace(GL20.GL_BACK);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glDepthMask(true);
    }

    @Override
    public void hide()
    {}

    @Override
    public void dispose()
    {
        mazeView.dispose();
        player.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        camera.viewportHeight = game.worldHeight;
        camera.viewportWidth = game.worldWidth;
        camera.update();
    }

    public void movePlayer(TileMaze.Direction velocity)
    {
        if(!player.isMoving())
        {
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

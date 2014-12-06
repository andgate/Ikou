package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.text.ParseException;

public class GameScreen extends ScreenAdapter
{
    private final Ikou game;
    private World world;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private Player player;
    private TileMap map;

    private InputMultiplexer im;

    public GameScreen(Ikou game)
            throws InvalidFileFormatException
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);

        Box2D.init();
        world = new World(new Vector2(0.0f, 0.0f), true);
        debugRenderer = new Box2DDebugRenderer();

        FileHandle file = Gdx.files.internal("data/level/1.txt");
        map = TileMapParser.parse(file.readString(), world);

        player = new Player(game, world, map.getStartPosition());

        im = new InputMultiplexer();
        im.addProcessor(new PlayerDirectionGestureDetector(player));
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void show()
    {}

    @Override
    public void render(float delta)
    {
        renderSetup();
        map.render(camera);
        player.render(camera);
        debugRenderer.render(world, camera.combined);

        doPhysicsStep(delta);
    }

    private void renderSetup()
    {
        Gdx.gl20.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private float accumulator = 0.0f;

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
            accumulator -= Constants.TIME_STEP;
        }
    }

    @Override
    public void hide()
    {}

    @Override
    public void dispose()
    {
        player.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);
        camera.update();
    }
}

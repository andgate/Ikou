package com.andgate.ikou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen extends ScreenAdapter
{
    private final Ikou game;
    private World world;
    public SpriteBatch batch;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private Player player;

    public GameScreen(Ikou game)
    {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.worldWidth, game.worldHeight);

        batch = new SpriteBatch();

        Box2D.init();
        world = new World(new Vector2(0.0f, 0.0f), true);
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(game, world);
    }

    @Override
    public void show()
    {}

    @Override
    public void render(float delta)
    {
        renderSetup();
        debugRenderer.render(world, camera.combined);

        doPhysicsStep(delta);
    }

    private void renderSetup()
    {
        Gdx.gl20.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // tell the camera to update its matrices.
        camera.update();
        batch.setProjectionMatrix(camera.combined);
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
        world.dispose();
    }
}

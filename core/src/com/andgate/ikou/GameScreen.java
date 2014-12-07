package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

public class GameScreen extends ScreenAdapter
{
    private final Ikou game;
    private CameraInputController camController;
    private DebugDrawer debugDrawer;

    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    btBroadphaseInterface broadphase;
    btDynamicsWorld dynamicsWorld;
    btConstraintSolver constraintSolver;

    //private OrthographicCamera camera;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    private Player player;
    private TileMap map;

    private InputMultiplexer im;

    public GameScreen(Ikou game)
            throws InvalidFileFormatException
    {
        this.game = game;

        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, game.worldWidth, game.worldHeight);

        createEnvironment();
        modelBatch = new ModelBatch();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));

        debugDrawer = new DebugDrawer();
        dynamicsWorld.setDebugDrawer(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        FileHandle file = Gdx.files.internal("data/level/1.txt");
        map = TileMapParser.parse(file.readString(), dynamicsWorld);
        player = new Player(game, map.getStartPosition(), dynamicsWorld);

        createCamera();

        im = new InputMultiplexer();
        im.addProcessor(new PlayerDirectionGestureDetector(player));
        //im.addProcessor(camController);
        Gdx.input.setInputProcessor(im);
    }

    private void createCamera()
    {
        camera = new PerspectiveCamera(67, game.worldWidth, game.worldHeight);
        camera.position.set(player.getX(), player.getY() + 3.0f, player.getZ() - 3.0f);
        camera.lookAt(0,0,0);
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
        //camera.transform(player.getTransform());
        camera.position.set(player.getPosition());
        camera.position.y += 3.0;
        camera.position.z -= 3.0;
        camera.lookAt(player.getPosition());
        camera.update();

        renderSetup();
        modelBatch.begin(camera);
            map.render(modelBatch, environment);
            player.render(modelBatch, environment);
        modelBatch.end();

        renderDebug();

        doPhysicsStep(delta);

        player.update(delta);
        map.update();
    }

    private void renderSetup()
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    private void renderDebug()
    {
        debugDrawer.begin(camera);
            dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
    }

    private float accumulator = 0.0f;

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            // Step
            dynamicsWorld.stepSimulation(Constants.TIME_STEP, 5, 1f/60f);
            accumulator -= Constants.TIME_STEP;
        }
    }

    @Override
    public void hide()
    {}

    @Override
    public void dispose()
    {
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
        map.dispose();
        player.dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        camera.viewportHeight = game.worldHeight;
        camera.viewportWidth = game.worldWidth;
        camera.update();
    }
}

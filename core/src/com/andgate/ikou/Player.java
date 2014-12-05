package com.andgate.ikou;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Vector;

public class Player
{
    private final Ikou game;
    private final World world;
    private final ShapeRenderer shapeRenderer;
    public Body body;

    private static final float LENGTH = 1.0f;

    public Player(Ikou game, World world)
    {
        this.game = game;
        this.world = world;
        shapeRenderer = new ShapeRenderer();

        createBody(1.0f, 1.0f);
    }

    private void createBody(float x, float y)
    {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        // Create our body in the world using our body definition
        body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        PolygonShape box = new PolygonShape();
        float halfWidth = LENGTH / 2.0f;
        float halfHeight = LENGTH / 2.0f;
        float originX = halfWidth;
        float originY = halfHeight;
        Vector2 origin = new Vector2(originX, originY);
        float angle = 0.0f;
        box.setAsBox(halfWidth, halfHeight, origin, angle);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        box.dispose();
    }

    public void update(float delta)
    {
        // Do some update magic?
    }

    public void render(Camera camera)
    {
        float width = getWidth();
        float height = getHeight();
        float x = getX();
        float y = getY();

        //camera.lookAt(x, y, 0.0f);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    public float getX()
    {
        return body.getPosition().x;
    }

    public float getY()
    {
        return body.getPosition().y;
    }

    public float getWidth()
    {

        return LENGTH;
    }

    public float getHeight()
    {
        return LENGTH;
    }

    public void dispose()
    {
        shapeRenderer.dispose();
    }
}

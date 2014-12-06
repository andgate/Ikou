package com.andgate.ikou.tiles;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Tile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class ObstacleTileBehavior implements TileBehavior
{
    @Override
    public void create(World world, Tile tile)
    {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set our body's starting position in the world
        bodyDef.position.set(tile.getX(), tile.getY());
        bodyDef.fixedRotation = true;

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

        // Points on a box
        final float length = Constants.TILE_LENGTH;
        final float x1 = 0.0f;
        final float x2 = x1 + length;
        final float y1 = 0.0f;
        final float y2 = y1 + length;

        // Create vertices for a box
        Vector2 bottomLeftCorner = new Vector2(x1, y1);
        Vector2 bottomRightCorner = new Vector2(x2, y1);
        Vector2 topRightCorner = new Vector2(x2, y2);
        Vector2 topLeftCorner = new Vector2(x1, y2);

        // Use those vertices to create a box chainshape
        ChainShape box = new ChainShape();
        box.createLoop(new Vector2[]{bottomLeftCorner, bottomRightCorner, topRightCorner, topLeftCorner});

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

    @Override
    public void update()
    {

    }

    @Override
    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(Color.DARK_GRAY);

        // Points on a box
        final float thickness = Constants.MEDIUM_THICKNESS;
        final float width = Constants.TILE_LENGTH;
        final float height = Constants.TILE_LENGTH;
        final float x = 0.0f;
        final float y = 0.0f;

        // Create vertices for a box
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(x+thickness, y+thickness, width-(thickness*2.0f), height-(thickness*2.0f));
    }
}

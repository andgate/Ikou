package com.andgate.ikou;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class TileMap
{
    private final World world;
    private int width;
    private int height;
    private Body mapBounds;

    private ArrayList<Tile> tiles;

    private ShapeRenderer shapeRenderer;

    public TileMap(ArrayList<Tile> tiles, World world, Vector2 startPosition)
    {
        this.tiles = tiles;
        this.world = world;
        shapeRenderer = new ShapeRenderer();

        Tile lastTile = tiles.get(tiles.size() - 1);
        width = lastTile.getX() + 1;
        height = lastTile.getY() + 1;

        createMapBounds();
    }

    public void createMapBounds()
    {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(1, 1);
        bodyDef.fixedRotation = true;
        bodyDef.active = false;

        // Create our body in the world using our body definition
        mapBounds = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        PolygonShape mapBoundsShape = new PolygonShape();
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;
        float originX = halfWidth;
        float originY = halfHeight;
        Vector2 origin = new Vector2(originX, originY);
        float angle = 0.0f;
        mapBoundsShape.setAsBox(halfWidth, halfHeight, origin, angle);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = mapBoundsShape;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        // Create our fixture and attach it to the body
        Fixture fixture = mapBounds.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        mapBoundsShape.dispose();
    }

    public void render(Camera camera)
    {
        for(Tile tile : tiles)
        {
            tile.render(shapeRenderer);
        }
    }
}

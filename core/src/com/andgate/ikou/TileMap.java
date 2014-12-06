package com.andgate.ikou;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
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
    private Vector2 startPosition;

    private ArrayList<Tile> tiles;

    private ShapeRenderer shapeRenderer;

    public TileMap(ArrayList<Tile> tiles, World world, Vector2 startPosition)
    {
        this.tiles = tiles;
        this.world = world;
        this.startPosition = startPosition;
        shapeRenderer = new ShapeRenderer();

        Tile lastTile = tiles.get(tiles.size() - 1);
        width = lastTile.getX() + 1;
        height = lastTile.getY() + 1;

        createMapBounds();
        createTiles();
    }

    private void createTiles()
    {
        for(Tile tile : tiles)
        {
            tile.create(world);
        }
    }

    private void createMapBounds()
    {
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set our body's starting position in the world
        bodyDef.position.set(0, 0);
        bodyDef.fixedRotation = true;
        bodyDef.active = true;

        // Create our body in the world using our body definition
        mapBounds = world.createBody(bodyDef);

        // Create vertices for a rectangle chainshape
        Vector2 bottomLeftCorner = new Vector2(0.0f, 0.0f);
        Vector2 bottomRightCorner = new Vector2(width, 0.0f);
        Vector2 topRightCorner = new Vector2(width, height);
        Vector2 topLeftCorner = new Vector2(0.0f, height);

        // Use those vertices to create a rectangle chainshape
        ChainShape mapBoundsShape = new ChainShape();
        mapBoundsShape.createLoop(new Vector2[]{bottomLeftCorner, bottomRightCorner, topRightCorner, topLeftCorner});

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
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeType.Filled);

        for(Tile tile : tiles)
        {
            tile.render(shapeRenderer);
        }

        shapeRenderer.end();
    }

    public Vector2 getStartPosition()
    {
        return startPosition;
    }
}

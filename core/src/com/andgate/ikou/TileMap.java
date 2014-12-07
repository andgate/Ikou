package com.andgate.ikou;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class TileMap
{
    private static final float RIM_LENGTH = Constants.TILE_LENGTH / 10.0f;
    private static final float RIM_THICKNESS = Constants.TILE_LENGTH / 10.0f;
    private static final float RIM_HEIGHT = RIM_THICKNESS * 2.0f;

    private int width;
    private int length;
    private Vector2 startPosition;

    private ArrayList<Tile> tiles;

    private Model rimModel;
    private Array<ModelInstance> rimInstances;
    private btCollisionShape rimShape;
    private btCollisionObject rimObject;
    private btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    private btRigidBody rimBody;

    public TileMap(ArrayList<Tile> tiles, Vector2 startPosition, btDynamicsWorld dynamicsWorld)
    {
        this.tiles = tiles;
        this.startPosition = startPosition;

        Tile lastTile = tiles.get(tiles.size() - 1);
        width = lastTile.getX() + 1;
        length = lastTile.getY() + 1;

        createMapRim(dynamicsWorld);
        createTiles(dynamicsWorld);
    }

    private void createTiles(btDynamicsWorld dynamicsWorld)
    {
        for(Tile tile : tiles)
        {
            tile.create(dynamicsWorld);
        }
    }

    private void createMapRim(btDynamicsWorld dynamicsWorld)
    {
        /*ModelBuilder modelBuilder = new ModelBuilder();
        rimModel = modelBuilder.createBox(RIM_LENGTH, RIM_HEIGHT, RIM_THICKNESS, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        rimInstances = new Array<>();
        for(Tile tile : tiles)
        {

            rimInstances.add(new ModelInstance(rimModel));
            rimInstance..transform.setTranslation(0.0f, 0.0f, 0.0f);
        }*/
    }

    public void update()
    {
        for(Tile tile : tiles)
        {
            tile.update();
        }
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        for(Tile tile : tiles)
        {
            tile.render(modelBatch, environment);
        }
    }

    public Vector2 getStartPosition()
    {
        return startPosition;
    }

    public void dispose()
    {
        for(Tile tile : tiles)
        {
            tile.dispose();
        }
    }
}

package com.andgate.ikou;

import com.andgate.ikou.tiles.TileBehavior;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;

public class Tile
{
    public TileBehavior behavior;
    private int x;
    private int y;
    private static final float length = Constants.TILE_LENGTH;
    private static final float height = Constants.TILE_LENGTH / 10.0f;
    private Model tileModel;
    private ModelInstance tileInstance;
    private btCollisionShape tileShape;
    private btCollisionObject tileObject;
    private btRigidBodyConstructionInfo constructionInfo;

    private btRigidBody body;

    public Tile(int x, int y, TileBehavior behavior)
    {
        this.x = x;
        this.y = y;
        this.behavior = behavior;

        createModel();
        createBody();
    }

    public void create(btDynamicsWorld dynamicsWorld)
    {
        behavior.create(this);
        dynamicsWorld.addRigidBody(body);
    }

    public void createModel()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        tileModel = modelBuilder.createBox(length, height, length, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)), Usage.Position | Usage.Normal);
        tileInstance = new ModelInstance(tileModel);
        tileInstance.transform.setTranslation(x, 0.0f, y);
    }

    public void createBody()
    {
        tileShape = new btBoxShape(new Vector3(length, height, length));
        tileObject = new btCollisionObject();
        tileObject.setCollisionShape(tileShape);
        tileObject.setWorldTransform(tileInstance.transform);

        // Create a rigid body for the tile
        float mass = 0.0f;
        Vector3 localInertia = new Vector3();
        if (mass > 0f)
        {
            tileShape.calculateLocalInertia(mass, localInertia);
        }
        else
        {
            localInertia.set(0, 0, 0);
        }
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, tileShape, localInertia);
        body = new btRigidBody(constructionInfo);
        body.setWorldTransform(tileInstance.transform);
    }

    public void update()
    {
        behavior.update();
        body.getWorldTransform(tileInstance.transform);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(tileInstance, environment);
        //behavior.render(shapeRenderer);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public btRigidBody getBody()
    {
        return body;
    }

    public void dispose()
    {
        behavior.dispose();
        tileModel.dispose();
    }
}

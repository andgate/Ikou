package com.andgate.ikou;

import com.badlogic.gdx.graphics.Camera;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;

public class Player implements DirectionListener, Disposable
{
    private final Ikou game;
    private Model tileModel;
    private ModelInstance tileInstance;
    private btCollisionShape tileShape;
    private btCollisionObject tileObject;
    private btRigidBodyConstructionInfo constructionInfo;

    private static final float length = Constants.TILE_LENGTH;
    private static final float height = Constants.TILE_LENGTH / 10.0f;

    private btRigidBody body;

    private static final float PLAYER_SPEED = Constants.WORLD_LENGTH * 2.0f;

    public Player(Ikou game, Vector2 startPosition, btDynamicsWorld dynamicsWorld)
    {
        this.game = game;

        // Create the model for the tile
        ModelBuilder modelBuilder = new ModelBuilder();
        tileModel = modelBuilder.createBox(length, height, length,
                new Material(ColorAttribute.createDiffuse(Color.CYAN)),
                Usage.Position | Usage.Normal);
        tileInstance = new ModelInstance(tileModel);
        tileInstance.transform.setTranslation(startPosition.x, 3.0f, startPosition.y);

        // Create the collision object for the tile
        tileShape = new btBoxShape(new Vector3(length, height, length));
        tileObject = new btCollisionObject();
        tileObject.setCollisionShape(tileShape);
        tileObject.setWorldTransform(tileInstance.transform);

        // Create a rigid body for the tile
        float mass = 1.0f;
        Vector3 localInertia = new Vector3();
        if (mass > 0f)
        {
            tileShape.calculateLocalInertia(mass, localInertia);
        }
        else
        {
            localInertia.set(0, 0, 0);
        }
        constructionInfo = new btRigidBodyConstructionInfo(mass, null, tileShape, localInertia);
        body = new btRigidBody(constructionInfo);
        dynamicsWorld.addRigidBody(body);
        body.setWorldTransform(tileInstance.transform);
    }

    public void update(float delta)
    {
        body.getWorldTransform(tileInstance.transform);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(tileInstance, environment);
    }

    public float getX()
    {
        return 0.0f;
    }

    public float getY()
    {
        return 0.0f;
    }

    public float getWidth()
    {
        return Constants.TILE_LENGTH;
    }

    public float getHeight()
    {
        return Constants.TILE_LENGTH;
    }

    public void onLeft()
    {
        move(new Vector2(-PLAYER_SPEED, 0.0f));
    }

    public void onRight()
    {
        move(new Vector2(PLAYER_SPEED, 0.0f));
    }

    public void onUp()
    {
        move(new Vector2(0.0f, PLAYER_SPEED));
    }

    public void onDown()
    {
        move(new Vector2(0.0f, -PLAYER_SPEED));
    }

    private void move(Vector2 velocity)
    {
        /*Vector2 currentVelocity = body.getLinearVelocity();
        currentVelocity.len();
        if(currentVelocity.len() <= (0.0 + Constants.EPSILON))
        {
            body.setLinearVelocity(velocity);
        }*/
    }

    public btRigidBody getBody()
    {
        return body;
    }

    @Override
    public void dispose()
    {
        constructionInfo.dispose();
        body.dispose();
        tileObject.dispose();
        tileShape.dispose();
        tileModel.dispose();
    }
}

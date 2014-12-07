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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;

public class Player implements DirectionListener, Disposable
{
    private static final float length = Constants.TILE_LENGTH;
    private static final float height = Constants.TILE_LENGTH / 10.0f;

    private final Ikou game;
    private Model tileModel;
    private ModelInstance tileInstance;
    private btCollisionShape tileShape;
    private btCollisionObject tileObject;
    private btRigidBodyConstructionInfo constructionInfo;
    private btRigidBody body;

    private static final float PLAYER_SPEED = Constants.WORLD_LENGTH * 2.0f;

    public Player(Ikou game, Vector2 startPosition, btDynamicsWorld dynamicsWorld)
    {
        this.game = game;

        // Create the model for the tile
        ModelBuilder modelBuilder = new ModelBuilder();
        Color playerColor = Color.CYAN;
        playerColor.a = 0.6f;
        tileModel = modelBuilder.createBox(length, height, length,
                new Material(ColorAttribute.createDiffuse(playerColor)),
                Usage.Position | Usage.Normal);
        tileInstance = new ModelInstance(tileModel);
        tileInstance.transform.setTranslation(startPosition.x, 3.0f, startPosition.y);

        // Create the collision object for the tile
        //tileShape = new btBoxShape(new Vector3(length/2.0f, height/2.0f, length/2.0f));
        tileShape = new btCylinderShape(new Vector3(length/2.0f, height/2.0f, length/2.0f));
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
        constructionInfo.setFriction(0.0f);
        constructionInfo.setRestitution(0.0f);
        body = new btRigidBody(constructionInfo);
        dynamicsWorld.addRigidBody(body);
        body.setWorldTransform(tileInstance.transform);
        body.setCcdMotionThreshold(0.1f);
        body.setCcdSweptSphereRadius(0.02f);
        body.setInvInertiaDiagLocal(new Vector3(0.0f, 0.0f, 0.0f));
    }

    public void update(float delta)
    {
        body.getWorldTransform(tileInstance.transform);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(tileInstance, environment);
    }

    public Matrix4 getTransform()
    {
        return body.getWorldTransform();
    }

    public Vector3 getPosition()
    {
        return body.getCenterOfMassPosition();
    }

    public float getX()
    {
        return 0.0f;
    }

    public float getY()
    {
        return 0.1f;
    }

    public float getZ()
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
        move(new Vector3(PLAYER_SPEED, 0.0f, 0.0f));
    }

    public void onRight()
    {
        move(new Vector3(-PLAYER_SPEED, 0.0f, 0.0f));
    }

    public void onUp()
    {
        move(new Vector3(0.0f, 0.0f, PLAYER_SPEED));
    }

    public void onDown()
    {
        move(new Vector3(0.0f, 0.0f, -PLAYER_SPEED));
    }

    private void move(Vector3 velocity)
    {
        Vector3 currentVelocity = body.getLinearVelocity();
        currentVelocity.len();
        //body.isActive()
        //if(currentVelocity.len() <= (0.0 + Constants.EPSILON))
        {
            body.setLinearVelocity(velocity);
            body.activate();
        }

        System.out.println("Player Moved: " + velocity.toString());
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

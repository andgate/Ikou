package com.andgate.ikou.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class Obstacle extends Tile
{
    Tile obstacleTile;
    public Obstacle(Vector3 position)
    {
        super(position);
        obstacleTile = new Tile(position);
        obstacleTile.translate(new Vector3(0.0f, Tile.HEIGHT, 0.0f));

        Material playerMaterial = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        obstacleTile.tileModelInstance.materials.get(0).set(playerMaterial);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        super.render(modelBatch, environment);
        obstacleTile.render(modelBatch, environment);
    }
}

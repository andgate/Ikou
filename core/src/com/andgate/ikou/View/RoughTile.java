package com.andgate.ikou.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class RoughTile extends Tile
{
    public RoughTile(Vector3 position)
    {
        super(position);

        Material playerMaterial = new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY));
        tileModelInstance.materials.get(0).set(playerMaterial);
    }
}

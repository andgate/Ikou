package com.andgate.ikou.Render;

import com.andgate.ikou.Tiles.TileData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class PlayerModelBuilder
{
    public static Model build()
    {
        ModelBuilder modelBuilder = new ModelBuilder();
        int playerAttributes = Usage.Position | Usage.Normal;
        Material playerMaterial = new Material(
                ColorAttribute.createDiffuse(Color.CYAN),
                ColorAttribute.createSpecular(Color.BLUE),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        );

        return modelBuilder.createBox(TileData.WIDTH, TileData.HEIGHT, TileData.DEPTH, playerMaterial, playerAttributes);
    }
}

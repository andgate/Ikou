package com.andgate.ikou.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class RoughTileData extends TileData
{
    public RoughTileData()
    {
        super();
        tileMaterial = new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY));
    }
}

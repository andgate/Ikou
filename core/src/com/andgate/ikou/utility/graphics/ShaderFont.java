package com.andgate.ikou.utility.graphics;

import com.andgate.ikou.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShaderFont extends BitmapFont
{
    float scale = 1.0f;

    public ShaderFont(String fntFilename, String pngFilename)
    {
        super(Gdx.files.internal(fntFilename), loadTextureRegion(pngFilename));
        super.setOwnsTexture(true);
    }

    @Override
    public void setScale(float scale)
    {
        super.setScale(scale);
    }

    public float getScale()
    {
        return scale;
    }

    public void resetScale()
    {
        setScale ( 1.0f / getScale() );
    }

    private static TextureRegion loadTextureRegion(String pngFilename)
    {
        Texture texture = new Texture(Gdx.files.internal(pngFilename), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        return new TextureRegion(texture);
    }
}

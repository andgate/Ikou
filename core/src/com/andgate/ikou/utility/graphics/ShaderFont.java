/*
    This file is part of Ikou.
    Ikou is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License.
    Ikou is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with Ikou.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.andgate.ikou.utility.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderFont extends BitmapFont
{
    float scale = 1.0f;
    private ShaderProgram shader;

    public ShaderFont(String fntFilename, String pngFilename, ShaderProgram shader)
    {
        super(Gdx.files.internal(fntFilename), loadTextureRegion(pngFilename));
        super.setOwnsTexture(true);
        this.shader = shader;
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

    @Override
    public TextBounds draw (Batch batch, CharSequence str, float x, float y)
    {
        batch.setShader(shader);
        TextBounds bounds = super.draw(batch, str, x, y);
        batch.setShader(null);

        return bounds;
    }
}

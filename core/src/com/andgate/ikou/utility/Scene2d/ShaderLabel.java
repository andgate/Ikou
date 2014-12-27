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

package com.andgate.ikou.utility.Scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ShaderLabel extends Label
{
    private ShaderProgram shader;

    public ShaderLabel(CharSequence text, LabelStyle style, ShaderProgram shader) {
        super(text, style);
        this.shader = shader;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.setShader(shader);
        super.draw(batch, parentAlpha);
        batch.setShader(null);
    }

    public void setShaderProgram(ShaderProgram shader) {
        this.shader = shader;
    }
}
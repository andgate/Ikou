package com.andgate.ikou.utility.Scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ShaderTextButton extends TextButton
{
    private ShaderProgram shader;

    public ShaderTextButton(String text, TextButtonStyle style, ShaderProgram shader) {
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

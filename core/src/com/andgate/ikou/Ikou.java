package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.text.ParseException;

public class Ikou extends Game
{
    public float ppm = 0.0f;
    public float worldWidth = 0.0f;
    public float worldHeight = (float)Constants.WORLD_HEIGHT;
	
	@Override
	public void create ()
    {
        Gdx.graphics.setVSync(true);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        try
        {
            setScreen(new GameScreen(this));
        }
        catch(InvalidFileFormatException e)
        {
            System.out.println(e.getMessage());
        }
	}

	@Override
	public void render () {
		super.render();
	}

    @Override
    public void dispose()
    {
        getScreen().dispose();
    }

    @Override
    public void resize(int width, int height)
    {
        ppm = (float)Gdx.graphics.getHeight() / worldHeight;
        worldWidth = worldHeight * (float)width / (float)height;
    }
}

package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;

public class Ikou extends Game
{
    public float ppm = 0.0f;
    public float worldWidth = 0.0f;
    public float worldHeight = 0.0f;

    public Ikou()
    {
        Bullet.init();
    }
	
	@Override
	public void create ()
    {
        Gdx.graphics.setVSync(true);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

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
        float res = (float)width / (float)height;

        if(width <= height)
        {
            worldWidth = Constants.WORLD_LENGTH;
            worldHeight = worldWidth * (float)height / (float)width;
        }
        else
        {
            worldHeight = Constants.WORLD_LENGTH;
            worldWidth = worldHeight * (float)width / (float)height;
        }

        ppm = (float)Gdx.graphics.getHeight() / worldHeight;

        if(getScreen() != null)
        {
            getScreen().resize(width, height);
        }
    }
}

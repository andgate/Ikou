package com.andgate.ikou;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

        setScreen(new GameScreen(this));
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
        ppm = (float)Constants.WORLD_HEIGHT / (float)Gdx.graphics.getHeight();
        worldWidth = worldHeight * (float)width / (float)height;
    }
}

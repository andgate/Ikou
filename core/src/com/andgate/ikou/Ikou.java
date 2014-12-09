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
        //Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

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

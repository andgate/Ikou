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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Ikou extends Game
{
    private static final String TAG = "Ikou Main Class";
    public float ppm = 0.0f;
    public float worldWidth = 0.0f;
    public float worldHeight = 0.0f;

    public Skin skin;


    public FreeTypeFontGenerator logoFontGenerator;
    public BitmapFont logoFont;

    public FreeTypeFontGenerator menuFontGenerator;
    public BitmapFont menuTitleFont;
    public BitmapFont menuOptionFont;

    public Ikou()
    {
    }
	
	@Override
	public void create ()
    {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setCatchBackKey(true);
        Gdx.graphics.setVSync(true);
        screenAdjustments(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        skin = new Skin(Gdx.files.internal(Constants.SKIN_LOCATION));
        createFontGenerators();
        createFonts();

        setScreen(new MainMenuScreen(this));
	}

    private void createFontGenerators()
    {
        logoFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.LOGO_FONT_LOCATION));
        menuFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.MENU_FONT_LOCATION));
    }

    private void createFonts()
    {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = (int) ((float)Constants.LOGO_FONT_SIZE * ppm);
        logoFont = logoFontGenerator.generateFont(parameter);

        parameter.size = (int) ((float)Constants.MENU_TITLE_FONT_SIZE * ppm);
        menuTitleFont = menuFontGenerator.generateFont(parameter);

        parameter.size = (int) ((float)Constants.MENU_OPTION_FONT_SIZE * ppm);
        menuOptionFont = menuFontGenerator.generateFont(parameter);
    }

    @Override
    public void dispose()
    {
        disposeFonts();
        disposeFontGenerators();

        if(getScreen() != null)
            getScreen().dispose();
    }

    private void disposeFontGenerators()
    {
        if(logoFontGenerator != null)
            logoFontGenerator.dispose();
        if(menuFontGenerator != null)
            menuFontGenerator.dispose();
    }

    private void disposeFonts()
    {
        if(menuOptionFont != null)
            menuOptionFont.dispose();
        if(menuTitleFont != null)
            menuTitleFont.dispose();
        if(logoFont != null)
            logoFont.dispose();
    }


    @Override
    public void render () {
        super.render();
    }

    @Override
    public void resize(int width, int height)
    {
        screenAdjustments(width, height);

        disposeFonts();
        createFonts();

        if(getScreen() != null)
        {
            getScreen().resize(width, height);
        }
    }

    public void screenAdjustments(int width, int height)
    {
        float res = (float)width / (float)height;

        if(width <= height)
        {
            worldWidth = Constants.WORLD_LENGTH;
            ppm = (float)Gdx.graphics.getWidth() / worldWidth;
            worldHeight = worldWidth * (float)height / (float)width;
        }
        else
        {
            worldHeight = Constants.WORLD_LENGTH;
            ppm = (float)Gdx.graphics.getHeight() / worldHeight;
            worldWidth = worldHeight * (float)width / (float)height;
        }
    }
}

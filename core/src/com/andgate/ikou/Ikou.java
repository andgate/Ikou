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

import com.andgate.ikou.utility.graphics.ShaderFont;
import com.andgate.ikou.view.MainMenuScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Ikou extends Game
{
    private static final String TAG = "Ikou Main Class";
    public float ppm = 0.0f;
    public float worldWidth = 0.0f;
    public float worldHeight = 0.0f;

    public Skin skin;

    public ShaderFont logoFont;
    public ShaderFont menuTitleFont;
    public ShaderFont menuOptionFont;

    public ShaderProgram fontShader;

    public Sound fallSound;
    public Sound hitSound;

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
        loadFonts();
        loadShader();
        fallSound = Gdx.audio.newSound(Gdx.files.internal(Constants.SOUND_FOLDER + "fall.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal(Constants.SOUND_FOLDER + "hit.wav"));

        setScreen(new MainMenuScreen(this));
	}

    private void loadFonts()
    {
        logoFont = new ShaderFont(Constants.LOGO_FONT_FNT, Constants.LOGO_FONT_PNG);
        menuTitleFont = new ShaderFont(Constants.MENU_FONT_FNT, Constants.MENU_FONT_PNG);
        menuOptionFont = new ShaderFont(Constants.MENU_FONT_FNT, Constants.MENU_FONT_PNG);
    }

    private void loadShader()
    {
        fontShader = new ShaderProgram(Gdx.files.internal(Constants.FONT_VERT_SHADER), Gdx.files.internal(Constants.FONT_FRAG_SHADER));
        if (!fontShader.isCompiled()) {
            Gdx.app.error(TAG + " fontShader", "compilation failed:\n" + fontShader.getLog());
        }
    }

    @Override
    public void dispose()
    {
        disposeFonts();

        if(getScreen() != null)
            getScreen().dispose();
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
        scaleFonts();

        if(getScreen() != null)
        {
            getScreen().resize(width, height);
        }
    }

    public void screenAdjustments(int width, int height)
    {
        float res = (float)width / (float)height;

        if(width < height)
        {
            worldWidth = Constants.WORLD_LENGTH;
            ppm = (float)Gdx.graphics.getWidth() / worldWidth;
            worldHeight = worldWidth / res;
        }
        else
        {
            worldHeight = Constants.WORLD_LENGTH;
            ppm = (float)Gdx.graphics.getHeight() / worldHeight;
            worldWidth = worldHeight * res;
        }
    }

    public void scaleFonts()
    {
        logoFont.resetScale();
        float logoFontScale = Constants.LOGO_FONT_SIZE / (logoFont.getCapHeight() / ppm);
        logoFont.setScale(logoFontScale);

        menuTitleFont.resetScale();
        float menuTitleFontScale = Constants.MENU_TITLE_FONT_SIZE / (menuTitleFont.getCapHeight() / ppm);
        menuTitleFont.setScale(menuTitleFontScale);

        menuOptionFont.resetScale();
        float menuOptionFontScale = Constants.MENU_OPTION_FONT_SIZE / (menuOptionFont.getCapHeight() / ppm);
        menuOptionFont.setScale(menuOptionFontScale);
    }
}

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

import com.andgate.ikou.view.HelpScreen;
import com.andgate.ikou.view.MainMenuScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Ikou extends Game
{
    private static final String TAG = "Ikou Main Class";
    public float ppm = 0.0f;
    public float worldWidth = 0.0f;
    public float worldHeight = 0.0f;

    public AssetManager manager;

    public BitmapFont logoFont;
    public BitmapFont menuTitleFont;
    public BitmapFont menuOptionFont;
    public BitmapFont helpFont;

    public Sound roughSound;
    public Sound fallSound;
    public Sound hitSound;

    final public boolean free;
    final public boolean debug;

    public Drawable whiteTransparentOverlay;
    public Drawable newGameButtonUp;
    public Drawable newGameButtonDown;
    public Drawable continueButtonUp;
    public Drawable continueButtonDown;
    public Drawable helpButtonUp;
    public Drawable helpButtonDown;

    public Ikou()
    {
        this(false, false);
    }

    public Ikou(boolean free, boolean debug)
    {
        this.free = free;
        this.debug = debug;
    }
	
	@Override
	public void create ()
    {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setCatchBackKey(true);
        Gdx.graphics.setVSync(true);
        screenAdjustments(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager = new AssetManager(resolver);
        loadShader();
        loadFonts();
        loadSounds();

        buildMainMenuButtonDrawables();

        setScreen(new MainMenuScreen(this));
	}

    private void loadSounds()
    {
        roughSound = Gdx.audio.newSound(Gdx.files.internal(Constants.SOUND_FOLDER + "rough.wav"));
        roughSound.play(0.0f);
        fallSound = Gdx.audio.newSound(Gdx.files.internal(Constants.SOUND_FOLDER + "fall.wav"));
        fallSound.play(0.0f);
        hitSound = Gdx.audio.newSound(Gdx.files.internal(Constants.SOUND_FOLDER + "hit.wav"));
        hitSound.play(0.0f);
    }

    private void loadFonts()
    {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        manager.load(Constants.LOGO_FONT_TTF, FreeTypeFontGenerator.class);
        manager.load(Constants.MENU_FONT_TTF, FreeTypeFontGenerator.class);

        buildFonts();
    }

    private void loadShader()
    {
        // Maybe have an ambient occlusion shader someday
    }

    private void buildMainMenuButtonDrawables()
    {
        whiteTransparentOverlay = buildSolidRectangleDrawable(new Color(1.0f, 1.0f, 1.0f, 0.5f));

        newGameButtonDown = buildSolidRectangleDrawable(Color.CYAN);
        newGameButtonUp = buildSolidRectangleDrawable(Color.LIGHT_GRAY);

        continueButtonDown = buildSolidRectangleDrawable(Color.ORANGE);
        continueButtonUp = buildSolidRectangleDrawable(Color.GRAY);

        helpButtonDown = buildSolidRectangleDrawable(Color.GREEN);
        helpButtonUp = buildSolidRectangleDrawable(Color.DARK_GRAY);
    }

    private Drawable buildSolidRectangleDrawable(Color color)
    {
        Pixmap mainMenuButtonPixmap = new Pixmap(12, 20, Pixmap.Format.RGBA8888);
        mainMenuButtonPixmap.setColor(color);
        mainMenuButtonPixmap.fill();
        TextureRegionDrawable mainMenuButtonDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(mainMenuButtonPixmap)));
        return mainMenuButtonDrawable;
    }

    @Override
    public void dispose()
    {
        manager.dispose();

        if(getScreen() != null)
            getScreen().dispose();
    }


    @Override
    public void render () {
        super.render();
    }

    @Override
    public void resize(int width, int height)
    {
        screenAdjustments(width, height);
        buildFonts();

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

    public void buildFonts()
    {
        int logoFontSize = (int)(Constants.LOGO_FONT_SIZE * ppm);
        logoFont = buildFont(Constants.LOGO_FONT_TTF, Constants.LOGO_FONT, logoFontSize);

        int menuTitleFontSize = (int)(Constants.MENU_TITLE_FONT_SIZE * ppm);
        menuTitleFont = buildFont(Constants.MENU_FONT_TTF, Constants.MENU_TITLE_FONT, menuTitleFontSize);

        int menuOptionFontSize = (int)(Constants.MENU_OPTION_FONT_SIZE * ppm);
        menuOptionFont = buildFont(Constants.MENU_FONT_TTF, Constants.MENU_OPTION_FONT, menuOptionFontSize);

        /*helpFont.resetScale();
        String helpText = HelpScreen.loadHelpScreenText();
        BitmapFont.TextBounds helpTextBounds = helpFont.getBounds(helpText);
        float helpFontScale = (4f / 5f) - (helpTextBounds.height / Gdx.graphics.getHeight());
        helpFont.setScale(helpFontScale);*/

        int helpFontSize = (int)(Constants.HELP_FONT_SIZE * ppm);
        helpFont = buildFont(Constants.MENU_FONT_TTF, Constants.HELP_FONT, helpFontSize);
    }

    private BitmapFont buildFont(String in_font_path, String out_font_path, int size)
    {
        FreeTypeFontLoaderParameter params = new FreeTypeFontLoaderParameter();
        params.fontFileName = in_font_path;
        params.fontParameters.size = size;
        //params.fontParameters.genMipMaps = true;

        if(manager.isLoaded(out_font_path)) manager.unload(out_font_path);
        manager.load(out_font_path, BitmapFont.class, params);
        manager.finishLoading();
        return manager.get(out_font_path, BitmapFont.class);
    }
}

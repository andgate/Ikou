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

import com.andgate.ikou.view.MainMenuScreen;
import com.andgate.ikou.utility.XorRandomGen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Ikou extends Game
{
    private static final String TAG = "Ikou Main Class";
    public float ppu = 0.0f;
    public float worldWidth = 0.0f;
    public float worldHeight = 0.0f;

    public BitmapFont arial_fnt;

    public Sound roughSound;
    public Sound fallSound;
    public Sound hitSound;

    final public boolean debug;

    public Drawable whiteTransparentOverlay;
    public Drawable newGameButtonUp;
    public Drawable newGameButtonDown;
    public Drawable continueButtonUp;
    public Drawable continueButtonDown;
    public Drawable helpButtonUp;
    public Drawable helpButtonDown;

    public Ikou(boolean debug)
    {
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
        arial_fnt = genBitmapFont(Constants.ARIAL_FONT_PATH, Constants.ARIAL_FONT_SIZE);
    }

    private void loadShader()
    {
        // TODO: Load AO shader(s)
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
        arial_fnt.dispose();

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
            ppu = (float)Gdx.graphics.getWidth() / worldWidth;
            worldHeight = worldWidth / res;
        }
        else
        {
            worldHeight = Constants.WORLD_LENGTH;
            ppu = (float)Gdx.graphics.getHeight() / worldHeight;
            worldWidth = worldHeight * res;
        }
    }

    private BitmapFont genBitmapFont(String font_path, int font_size)
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(font_path));
        FreeTypeFontParameter params = new FreeTypeFontParameter();

        // Font size should be fairly large
        params.size = font_size;

        // A linear filter allows for fairly smooth downscaling.
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;

        // Not setting incremental results in a lot of glyphs not fitting onto
        // the texture region pages. Eventually, for heavy text rendering,
        // incremental glyph generation may begin missing chracters.
        params.incremental = true;

        BitmapFont font = generator.generateFont(params);
        //generator.dispose();

        /*BitmapFont font = new BitmapFont(Gdx.files.internal(font_path));

        for(TextureRegion region : font.getRegions()) {
            region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }*/

        return font;
    }
}

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

package com.andgate.ikou.view;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;
import com.andgate.ikou.input.HelpScreenControllerListener;
import com.andgate.ikou.input.HelpScreenInputListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class HelpScreen extends ScreenAdapter
{
    private static final String TAG = "HelpScreen";

    private static final String HELP_SCREEN_TITLE = "Help";
    private static final String TAP_TO_RETURN = "Tap to return";

    private final Ikou game;
    private Stage stage;
    private InputMultiplexer im;

    private String helpText;
    private ScrollPane helpTextScrollPane;
    private float help_fnt_scale, menu_title_fnt_scale;

    private enum State
    { Start, Play, End }
    private State state = State.Start;

    private HelpScreenControllerListener helpScreenControllerListener;

    public HelpScreen(Ikou game)
    {
        this.game = game;
        stage = new Stage();

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);

        im = new InputMultiplexer();
        im.addProcessor(new HelpScreenInputListener(this));
        im.addProcessor(stage);
        Gdx.input.setInputProcessor(im);

        helpScreenControllerListener = new HelpScreenControllerListener(this);
        Controllers.addListener(helpScreenControllerListener);

        helpText = loadHelpScreenText();

        if(game.debug) Gdx.app.log(TAG, helpText);

        buildStage();
    }

    private void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        calc_font_sizes();

        final Label.LabelStyle titleLabelStyle = new Label.LabelStyle(game.arial_fnt, Color.ORANGE);
        final Label titleLabel = new Label(HELP_SCREEN_TITLE, titleLabelStyle);
        titleLabel.setFontScale(menu_title_fnt_scale);

        final Label.LabelStyle helpTextLabelStyle = new Label.LabelStyle(game.arial_fnt, Color.BLACK);
        final Label helpTextLabel = new Label(helpText, helpTextLabelStyle);
        helpTextLabel.setFontScale(help_fnt_scale);
        helpTextLabel.setWrap(true);
        helpTextLabel.setAlignment(Align.center);
        helpTextScrollPane = new ScrollPane(helpTextLabel);

        Table table = new Table();
        table.add(titleLabel).expandX().center().row();
        table.add(helpTextScrollPane).center().fill().row();
        table.setFillParent(true);

        table.setBackground(game.whiteTransparentOverlay);

        stage.addActor(table);
    }

    public static String loadHelpScreenText()
    {
        FileHandle helpTextFile = Gdx.files.internal(Constants.HELP_TEXT);
        return helpTextFile.readString();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
        update(delta);
    }

    private void update(float delta)
    {
        switch(state)
        {
            case Start:
                updateStart(delta);
                break;
            case Play:
                updatePlay(delta);
                break;
            case End:
                updateEnd(delta);
                break;
            default:
                break;
        }
    }

    private void updateStart(float delta)
    {
        state = State.Play;
    }

    private void updatePlay(float delta)
    {
        stage.act();
    }

    private void updateEnd(float delta)
    {
        game.setScreen(new MainMenuScreen(game));
        this.dispose();
    }

    public void end()
    {
        state = State.End;
    }

    @Override
    public void resize(int width, int height)
    {
        buildStage();
    }

    void calc_font_sizes()
    {
        float font_scale_factor = game.ppu / (float)Constants.ARIAL_FONT_SIZE;
        help_fnt_scale = Constants.HELP_FONT_UNIT_SIZE * font_scale_factor;

        menu_title_fnt_scale = Constants.MENU_TITLE_FONT_UNIT_SIZE * font_scale_factor;
    }

    @Override
    public void dispose()
    {
        if(helpScreenControllerListener != null)
            Controllers.removeListener(helpScreenControllerListener);
        if(stage != null)
            stage.dispose();
    }

    public void scroll(float percent)
    {
        helpTextScrollPane.setScrollPercentY(percent);
    }
}

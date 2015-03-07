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
import com.andgate.ikou.input.MainMenuScreenInputListener;
import com.andgate.ikou.utility.Scene2d.ShaderLabel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen
{
    private static final String TAG = "MainMenuScreen";

    private final Ikou game;
    private Stage stage;
    private InputMultiplexer im;

    private static final String NEW_GAME_BUTTON_TEXT = "New";
    private static final String CONTINUE_BUTTON_TEXT = "Continue";
    private static final String HELP_BUTTON_TEXT = "Help";

    private final boolean isNewGame;

    private enum State
    { Start, Play, End }
    private State state = State.Start;

    private enum Option
    { None, New, Continue, Help }
    private Option selectedOption = Option.None;

    public MainMenuScreen(final Ikou game) {
        this.game = game;
        stage = new Stage();

        im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(new MainMenuScreenInputListener(this));
        Gdx.input.setInputProcessor(im);

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);

        Preferences playerPrefs = Gdx.app.getPreferences(Constants.PLAYER_PREFS);
        isNewGame = !playerPrefs.contains(Constants.PLAYER_PREF_LEVEL_SEED);

        buildStage();
        Gdx.input.setCursorCatched(false);
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        final LabelStyle titleLabelStyle = new LabelStyle(game.logoFont, Color.CYAN);
        final ShaderLabel titleLabel = new ShaderLabel(Constants.GAME_NAME, titleLabelStyle, game.fontShader);

        Table menuButtonTable = buildMenuButtonTable();

        Table table = new Table();
            table.add(titleLabel).center().top().spaceBottom(25.0f).row();
            table.add(menuButtonTable).center();
        table.setFillParent(true);

        stage.addActor(table);
    }

    private Table buildMenuButtonTable()
    {
        final LabelStyle buttonLabelStyle = new LabelStyle(game.menuOptionFont, Color.WHITE);
        final ButtonStyle buttonStyle = new ButtonStyle(game.skin.getDrawable("default-round"),
                game.skin.getDrawable("default-round-down"),
                game.skin.getDrawable("default-round"));

        final ShaderLabel newGameButtonLabel = new ShaderLabel(NEW_GAME_BUTTON_TEXT, buttonLabelStyle, game.fontShader);
        final Button newGameButton = new Button(buttonStyle);
        newGameButton.add(newGameButtonLabel);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.buttonPressedSound.play();
                MainMenuScreen.this.setOption(Option.New);
                MainMenuScreen.this.end();
            }
        });

        final ShaderLabel continueButtonLabel = new ShaderLabel(CONTINUE_BUTTON_TEXT, buttonLabelStyle, game.fontShader);
        final Button continueButton = new Button(buttonStyle);
        continueButton.add(continueButtonLabel);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.buttonPressedSound.play();
                MainMenuScreen.this.setOption(Option.Continue);
                MainMenuScreen.this.end();
            }
        });

        final ShaderLabel helpButtonLabel = new ShaderLabel(HELP_BUTTON_TEXT, buttonLabelStyle, game.fontShader);
        final Button helpButton = new Button(buttonStyle);
        helpButton.add(helpButtonLabel);

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.buttonPressedSound.play();
                MainMenuScreen.this.setOption(Option.Help);
                MainMenuScreen.this.end();
            }
        });

        Table menuButtonsTable = new Table();
            menuButtonsTable.add(newGameButton).fill().spaceBottom(20.0f).row();
            if(!isNewGame) menuButtonsTable.add(continueButton).fill().spaceBottom(20.0f).row();
            menuButtonsTable.add(helpButton).fill();


        return menuButtonsTable;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        switch(selectedOption)
        {
            case New:
                game.setScreen(new GameScreen(game, true));
                break;
            case Continue:
                game.setScreen(new GameScreen(game, false));
                break;
            case Help:
                game.setScreen(new HelpScreen(game));
                break;
            default:
                Gdx.app.exit();
                return;
        }

        this.dispose();
    }

    public void end()
    {
        state = State.End;
    }

    public void setOption(Option option)
    {
        this.selectedOption = option;
    }

    @Override
    public void resize(int width, int height)
    {
        buildStage();
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        if(stage != null) stage.dispose();
    }
}
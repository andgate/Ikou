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
import com.andgate.ikou.input.MainMenuButtonClickListener;
import com.andgate.ikou.input.MainMenuControllerListener;
import com.andgate.ikou.input.MainMenuScreenInputListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainMenuScreen implements Screen
{
    private static final String TAG = "MainMenuScreen";

    private final Ikou game;
    private Preview preview;
    private Stage stage;
    private InputMultiplexer im;
    private MainMenuControllerListener mainMenuControllerListener;

    private static final String NEW_GAME_BUTTON_TEXT = "New Game";
    private static final String CONTINUE_BUTTON_TEXT = "Continue";
    private static final String HELP_BUTTON_TEXT = "Help";

    private static final int NEW_GAME_BUTTON = 0;
    private static final int CONTINUE_BUTTON = 1;
    private static final int HELP_BUTTON = 2;

    private float logo_fnt_scale, menu_option_fnt_scale;

    private TextButton[] buttons = new TextButton[3];

    private final boolean isNewGame;

    private enum State
    { Start, Play, End }
    private State state = State.Start;

    public enum Option
    {
        New(0), Continue(1), Help(2), None(3);
        private final int value;

        private Option(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private Option selectedOption = Option.None;

    private static Option[] options = Option.values();
    private Option getOption(int index)
    {
        return options[index];
    }

    public MainMenuScreen(final Ikou game) {
        this.game = game;
        preview = new Preview();
        stage = new Stage();

        im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(new MainMenuScreenInputListener(this));
        Gdx.input.setInputProcessor(im);
        Gdx.input.setCursorCatched(false);

        mainMenuControllerListener = new MainMenuControllerListener(this);
        Controllers.addListener(mainMenuControllerListener);

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);

        Preferences playerPrefs = Gdx.app.getPreferences(Constants.PLAYER_PREFS);
        isNewGame = !playerPrefs.contains(Constants.PLAYER_PREF_LEVEL_SEED);

        buildStage();
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        calc_font_sizes();

        final LabelStyle titleLabelStyle = new LabelStyle(game.arial_fnt, Constants.LOGO_FONT_COLOR);
        final Label titleLabel = new Label(Constants.GAME_NAME, titleLabelStyle);
        titleLabel.setFontScale(logo_fnt_scale);

        Table menuButtonTable = buildMenuButtonTable();

        Table table = new Table();
            table.add(titleLabel).expand().center().row();
            table.add(menuButtonTable).bottom().fill();
        table.setFillParent(true);
        table.setBackground(game.whiteTransparentOverlay);

        stage.addActor(table);
        //stage.setDebugAll(true);
    }

    private Table buildMenuButtonTable()
    {
        final TextButtonStyle newGameButtonStyle = new TextButtonStyle(game.newGameButtonUp,
                game.newGameButtonDown,
                game.newGameButtonUp,
                game.arial_fnt);
        final TextButtonStyle continueButtonStyle = new TextButtonStyle(game.continueButtonUp,
                game.continueButtonDown,
                game.continueButtonUp,
                game.arial_fnt);
        final TextButtonStyle helpButtonStyle = new TextButtonStyle(game.helpButtonUp,
                game.helpButtonDown,
                game.helpButtonUp,
                game.arial_fnt);


        buttons[NEW_GAME_BUTTON] = new TextButton(NEW_GAME_BUTTON_TEXT, newGameButtonStyle);
        final TextButton newGameButton = buttons[NEW_GAME_BUTTON];
        newGameButton.getLabel().setFontScale(menu_option_fnt_scale);
        newGameButton.addListener(new MainMenuButtonClickListener(this, Option.New));


        buttons[CONTINUE_BUTTON] = new TextButton(CONTINUE_BUTTON_TEXT, continueButtonStyle);
        final TextButton continueButton = buttons[CONTINUE_BUTTON];
        continueButton.getLabel().setFontScale(menu_option_fnt_scale);
        continueButton.addListener(new MainMenuButtonClickListener(this, Option.Continue));


        buttons[HELP_BUTTON] = new TextButton(HELP_BUTTON_TEXT, helpButtonStyle);
        final TextButton helpButton = buttons[HELP_BUTTON];
        helpButton.getLabel().setFontScale(menu_option_fnt_scale);
        helpButton.addListener(new MainMenuButtonClickListener(this, Option.Help));


        Table menuButtonsTable = new Table();
            menuButtonsTable.add(newGameButton).expand().fill().row();
            if(!isNewGame) menuButtonsTable.add(continueButton).fill().row();
            menuButtonsTable.add(helpButton).fill();

        return menuButtonsTable;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        preview.render();
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
        preview.update(delta);
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
        calc_font_sizes();
        buildStage();
        preview.resize(width, height);
    }

    private void calc_font_sizes()
    {
        float font_scale_factor = game.ppu / (float)Constants.ARIAL_FONT_SIZE;

        logo_fnt_scale = Constants.LOGO_FONT_UNIT_SIZE * font_scale_factor;
        menu_option_fnt_scale = Constants.MENU_OPTION_FONT_UNIT_SIZE * font_scale_factor;
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
        Controllers.removeListener(mainMenuControllerListener);
        preview.dispose();
    }

    public void selectNextOption()
    {
        int nextOptionIndex = selectedOption.getValue() + 1;
        if(nextOptionIndex == CONTINUE_BUTTON && isNewGame) nextOptionIndex += 1;
        if(nextOptionIndex > HELP_BUTTON) nextOptionIndex = NEW_GAME_BUTTON;

        setSelection(getOption(nextOptionIndex));
    }

    public void selectPreviousOption()
    {
        int nextOptionIndex = selectedOption.getValue() - 1;
        if(nextOptionIndex == CONTINUE_BUTTON && isNewGame) nextOptionIndex -= 1;
        if(nextOptionIndex < NEW_GAME_BUTTON) nextOptionIndex = HELP_BUTTON;

        setSelection(getOption(nextOptionIndex));
    }

    public void setSelection(Option newSelection)
    {
        cancelSelection();

        if(newSelection == Option.Continue && isNewGame) return;

        selectedOption = newSelection;
        int newOptionIndex = selectedOption.getValue();
        if(selectedOption != Option.None)
        {
            buttons[newOptionIndex].getClickListener().touchDown(null, 0, 0, 0, Input.Buttons.LEFT);
            stage.setKeyboardFocus(buttons[newOptionIndex]);
        }
    }

    public void cancelSelection()
    {
        if(selectedOption != Option.None)
        {
            TextButton currentButton = buttons[selectedOption.getValue()];
            currentButton.getClickListener().cancel();
            currentButton.getClickListener().touchUp(null, 0, 0, 0, Input.Buttons.LEFT);
            selectedOption = Option.None;
        }
    }

    public void confirmSelection()
    {
        if(selectedOption != Option.None)
        {
            MainMenuScreen.this.end();
        }
    }
}
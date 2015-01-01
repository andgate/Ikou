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

import com.andgate.ikou.Ikou;
import com.andgate.ikou.io.LevelLoader;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.utility.Scene2d.ShaderLabel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.io.IOException;

public class FloorSelectScreen extends ScreenAdapter
{
    private static final String TAG = "FloorSelectScreen";

    private final Ikou game;
    private Stage stage;

    private final LevelData levelData;

    private static final String SELECT_FLOOR_TEXT = "Select a Floor";

    private static final int COLUMNS = 7;

    public FloorSelectScreen(final Ikou newGame, final LevelData levelData)
    {
        game = newGame;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        this.levelData = levelData;

        buildStage();
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        final Label.LabelStyle titleLabelStyle = new Label.LabelStyle(game.menuTitleFont, Color.CYAN);
        final ShaderLabel titleLabel = new ShaderLabel(SELECT_FLOOR_TEXT, titleLabelStyle, game.fontShader);

        final Label.LabelStyle floorOptionLabelStyle = new Label.LabelStyle(game.menuOptionFont, Color.BLACK);
        final Label.LabelStyle lockedFloorOptionLabelStyle = new Label.LabelStyle(game.menuOptionFont, Color.GRAY);
        Table floorOptionsTable = new Table();


        float padding = 0.5f * game.ppm;
        float actorLength = (float)Gdx.graphics.getWidth() / COLUMNS - padding * 2.0f;

        for(int floorNumber = 1; floorNumber <= levelData.totalFloors; floorNumber++)
        {
            ShaderLabel floorLabel = null;
            if(floorNumber <= levelData.completedFloors+1)
            {
                floorLabel = new ShaderLabel("" + floorNumber, floorOptionLabelStyle, game.fontShader);
                floorLabel.addListener(new FloorOptionClickListener(game, this, levelData, floorNumber));
            }
            else
            {
                floorLabel = new ShaderLabel("?", lockedFloorOptionLabelStyle, game.fontShader);
            }

            floorOptionsTable.add(floorLabel).pad(padding).width(actorLength).height(actorLength).center();

            if(floorNumber % COLUMNS == 0)
            {
                floorOptionsTable.row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(floorOptionsTable);

        Table table = new Table();

        table.add(titleLabel).center().top().row();
        table.add(scrollPane).fill().expand().top().left();

        table.setFillParent(true);

        stage.addActor(table);
        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            gotoLevelSelect();
        }

        stage.act();
    }

    public void gotoLevelSelect()
    {
        game.setScreen(new LevelSelectScreen(game));
        dispose();
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
        if(stage != null)
            stage.dispose();
    }

    private class FloorOptionClickListener extends ClickListener
    {
        private static final String TAG = "FloorOptionClickListener";

        private final Ikou game;
        private final FloorSelectScreen screen;
        private final LevelData levelData;
        private final int floor;

        public FloorOptionClickListener(Ikou game, FloorSelectScreen screen, LevelData levelData, int floor)
        {
            super();

            this.game = game;
            this.screen = screen;
            this.levelData = levelData;
            this.floor = floor;
        }

        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            //game.buttonPressedSound.play();
            // Load level
            try
            {
                Level level = LevelLoader.load(levelData);
                game.setScreen(new GameScreen(game, level, floor));
                screen.dispose();
            }
            catch(final IOException e)
            {
                final String errorMessage = "Failed to load level " + levelData.name;
                Gdx.app.error(TAG, errorMessage, e);
            }
        }
    }
}

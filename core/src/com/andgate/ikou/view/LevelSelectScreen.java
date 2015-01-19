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
import com.andgate.ikou.io.LevelDatabaseService;
import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.utility.Scene2d.ShaderLabel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen
{
    private static final String TAG = "LevelSelectScreen";

    private final Ikou game;
    private Stage stage;
    private LevelData[] levelDatas;

    private static final String SELECT_LEVEL_TEXT = "Select Level";

    public LevelSelectScreen(final Ikou game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        levelDatas = LevelDatabaseService.getLevelDatas();

        buildStage();
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        final Label.LabelStyle titleLabelStyle = new Label.LabelStyle(game.menuTitleFont, Color.CYAN);
        final ShaderLabel titleLabel = new ShaderLabel(SELECT_LEVEL_TEXT, titleLabelStyle, game.fontShader);

        final Label.LabelStyle levelOptionLabelStyle = new Label.LabelStyle(game.menuOptionFont, Color.BLACK);
        Table levelOptionsTable = new Table();

        for(int i = 0; i < levelDatas.length; i++)
        {
            final Table levelInfoTable = new Table();

            LevelData levelData = levelDatas[i];

            final ShaderLabel levelNameLabel = new ShaderLabel(levelData.name, levelOptionLabelStyle, game.fontShader);

            final String levelCompletion = levelData.completedFloors + " out of " + levelData.totalFloors;
            final ShaderLabel levelProgressLabel = new ShaderLabel(levelCompletion, levelOptionLabelStyle, game.fontShader);

            levelInfoTable.addListener(new LevelOptionClickListener(game, this, levelData));

            levelInfoTable.add(levelNameLabel).row();
            levelInfoTable.add(levelProgressLabel);

            float sidePadding = 1.0f * game.ppm;
            levelOptionsTable.add(levelInfoTable).pad(0.0f, sidePadding, 0.0f, sidePadding);
        }


        ScrollPane scrollPane = new ScrollPane(levelOptionsTable);

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
            gotoMainMenu();
        }

        stage.act();
    }

    public void gotoMainMenu()
    {
        game.setScreen(new MainMenuScreen(game));
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

    private class LevelOptionClickListener extends ClickListener
    {
        private static final String TAG = "LevelOptionClickListener";

        private final Ikou game;
        private final LevelSelectScreen screen;
        private final LevelData levelData;

        public LevelOptionClickListener(Ikou game, LevelSelectScreen screen, LevelData levelData)
        {
            super();

            this.game = game;
            this.screen = screen;
            this.levelData = levelData;
        }

        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            //game.buttonPressedSound.play();
            game.setScreen(new FloorSelectScreen(game, levelData));
            screen.dispose();
        }
    }
}

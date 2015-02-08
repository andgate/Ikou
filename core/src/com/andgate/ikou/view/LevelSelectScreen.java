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
import com.andgate.ikou.io.LevelDatabaseService;
import com.andgate.ikou.io.LevelLoaderThread;
import com.andgate.ikou.model.Level;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen
{
    private static final String TAG = "LevelSelectScreen";

    private final Ikou game;
    private Stage stage;
    private LevelLoaderThread levelLoader;

    private LevelData[] levelDatas;

    private LevelData selectedLevelData = null;

    private static final String SELECT_LEVEL_HEADER = "Levels";
    private static final String SELECT_FLOOR_HEADER = "Floors";
    private static final String SELECT_LEVEL_MESSAGE = "Select a level";

    private ShaderLabel floorProgressLabel;
    private ShaderLabel floorSelectLabel;
    private ShaderLabel noLevelSelected;

    private Table floorSelectTable;

    private String floorProgressString = " ";

    public LevelSelectScreen(final Ikou game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);

        levelDatas = LevelDatabaseService.getLevelDatas();

        buildStage();
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // get the level and floor select tables ready
        final Table levelSelectorTable = buildLevelSelectorTable();
        floorSelectTable = buildFloorSelectTable();

        final Table table = new Table();
        table.add(levelSelectorTable).expand().fill();

        if(game.worldWidth < game.worldHeight)
            table.row();

        table.add(floorSelectTable).fill();

        table.setFillParent(true);

        stage.addActor(table);
        stage.setDebugAll(true);
    }

    private Table buildLevelSelectorTable()
    {
        final LabelStyle titleLabelStyle = new Label.LabelStyle(game.menuTitleFont, Color.CYAN);
        final ShaderLabel titleLabel = new ShaderLabel(SELECT_LEVEL_HEADER, titleLabelStyle, game.fontShader);

        Table levelsTable = buildLevelsTable();
        ScrollPane scrollPane = new ScrollPane(levelsTable);

        Table levelSelectorTable = new Table();
        levelSelectorTable.add(titleLabel).row();
        // Needs default/custom toggle switches for levels
        levelSelectorTable.add(scrollPane);

        return levelSelectorTable;
    }

    private Table buildLevelsTable()
    {

        String[] levelNames = new String[levelDatas.length];

        for(int i = 0; i < levelDatas.length; i++)
        {
            levelNames[i] = levelDatas[i].name;
        }

        final List.ListStyle levelNameListStyle = new List.ListStyle(game.skin.get(ListStyle.class));
        levelNameListStyle.font = game.menuOptionFont;
        List levelNameList = new List(levelNameListStyle);
        levelNameList.setItems(levelNames);


        float sidePadding = 1.0f * game.ppm;
        Table levelsTable = new Table();
        levelsTable.add(levelNameList).pad(0.0f, sidePadding, 0.0f, sidePadding).row();

        return levelsTable;
    }

    private Table buildFloorSelectTable()
    {
        Table floorSelectTable = new Table();

        final LabelStyle floorProgressStyle = new LabelStyle(game.menuOptionFont, Color.BLACK);
        floorProgressLabel = new ShaderLabel(floorProgressString, floorProgressStyle, game.fontShader);

        final LabelStyle floorsSelectLabelStyle = new LabelStyle(game.menuTitleFont, Color.CYAN);
        floorSelectLabel = new ShaderLabel(SELECT_FLOOR_HEADER, floorsSelectLabelStyle, game.fontShader);
        noLevelSelected = new ShaderLabel(SELECT_LEVEL_MESSAGE, floorsSelectLabelStyle, game.fontShader);

        floorSelectTable.add(floorSelectLabel).row();
        floorSelectTable.add(floorProgressLabel).row();
        floorSelectTable.add(noLevelSelected);

        // Add the level floor previewer
        //floorSelectorTable.add(scrollPane).fill().expand().top().left();

        //floorSelectTable.setFillParent(true);

        return floorSelectTable;
    }

    private Table buildFloorsTable()
    {
        Table floorsTable = new Table();

        return floorsTable;
    }

    public void setSelectedLevel(LevelData levelData)
    {
        if(levelLoader != null)
        {
            // Last loader really
            // just needs to go away.
            // This will do for now...
            while(levelLoader.isAlive())
            {
                // wait for the levelLoader to die
            }
        }

        floorProgressString = levelData.completedFloors + " / " + levelData.totalFloors;
        floorProgressLabel.setText(floorProgressString);

        levelLoader = new LevelLoaderThread(game, levelData);
        levelLoader.start();

        // remove the old floor selector
        // build a floor selector with the loaded level
        // add it to the floor selector table
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
            //game.setScreen(new FloorSelectScreen(game, levelData));
            //screen.dispose();
            screen.setSelectedLevel(levelData);
        }
    }
}

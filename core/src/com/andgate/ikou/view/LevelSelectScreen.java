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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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

    private List levelNameList;
    private ShaderLabel floorProgressLabel;
    private Container previewContainer;
    private LevelPreview levelPreview;

    private String floorProgressString = " ";

    public LevelSelectScreen(final Ikou game)
    {
        this.game = game;
        levelPreview = new LevelPreview();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

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
        final Table floorSelectTable = buildFloorSelectTable();

        final Table table = new Table();
        table.add(levelSelectorTable).expand().fill();

        if(game.worldWidth < game.worldHeight)
            table.row();

        table.add(floorSelectTable).fill().expand();

        table.setFillParent(true);

        stage.addActor(table);
        stage.setDebugAll(true);

        // Build the layout's dimensional information.
        table.pack();
        table.layout();

        // Then get the width/height of the container
        int w = (int)previewContainer.getWidth();
        int h = (int)previewContainer.getHeight();

        levelPreview.setSize(w, h);
        previewContainer.setBackground(levelPreview.getDrawable());
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

        int currentSelectedIndex = -1;
        if(levelNameList != null)
            currentSelectedIndex = levelNameList.getSelectedIndex();

        levelNameList = new List(levelNameListStyle);
        levelNameList.setItems(levelNames);
        levelNameList.setSelectedIndex(currentSelectedIndex);
        levelNameList.addListener(new LevelSelectInputListener(levelNameList, this, levelDatas));


        float sidePadding = 1.0f * game.ppm;
        Table levelsTable = new Table();
        levelsTable.add(levelNameList).pad(0.0f, sidePadding, 0.0f, sidePadding).row();

        return levelsTable;
    }

    private Table buildFloorSelectTable()
    {
        final Table floorSelectTable = new Table();

        final LabelStyle floorProgressStyle = new LabelStyle(game.menuOptionFont, Color.WHITE);
        floorProgressLabel = new ShaderLabel(floorProgressString, floorProgressStyle, game.fontShader);

        final LabelStyle floorsSelectLabelStyle = new LabelStyle(game.menuTitleFont, Color.CYAN);
        final ShaderLabel floorSelectLabel = new ShaderLabel(SELECT_FLOOR_HEADER, floorsSelectLabelStyle, game.fontShader);
        final ShaderLabel noLevelSelected = new ShaderLabel(SELECT_LEVEL_MESSAGE, floorProgressStyle, game.fontShader);

        previewContainer = new Container();

        if(levelNameList.getSelectedIndex() == -1)
            previewContainer.setActor(noLevelSelected);

        float length = (Gdx.graphics.getWidth() < Gdx.graphics.getHeight())
                ? Gdx.graphics.getWidth() : Gdx.graphics.getHeight();

        length *= 0.60f;
        previewContainer.size(length);

        floorSelectTable.add(floorSelectLabel).row();
        floorSelectTable.add(floorProgressLabel).row();
        floorSelectTable.add(previewContainer);

        return floorSelectTable;
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

        previewContainer.clear();

        floorProgressString = levelData.completedFloors + " / " + levelData.totalFloors;
        floorProgressLabel.setText(floorProgressString);

        levelLoader = new LevelLoaderThread(game, levelData);
        levelLoader.start();

        while(levelLoader.isAlive()) {}
        levelPreview.setLevelRender(levelLoader.getLevelRender());

        // remove the old floor selector
        // build a floor selector with the loaded level
        // add it to the floor selector table
    }

    @Override
    public void render(float delta)
    {
        levelPreview.render(delta);

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.draw();

        Gdx.gl.glFinish();

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

    private class LevelSelectInputListener extends InputListener
    {
        private static final String TAG = "LevelSelectInputListener";

        private final List levelList;
        private final LevelSelectScreen screen;
        private final LevelData[] levelDatas;

        private int selectedIndex = 0;

        public LevelSelectInputListener(List levelList, LevelSelectScreen screen, LevelData[] levelDatas)
        {
            super();

            this.levelList = levelList;
            this.screen = screen;
            this.levelDatas = levelDatas;

            selectedIndex = levelList.getSelectedIndex();
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
        {
            //game.buttonPressedSound.play();
            //game.setScreen(new FloorSelectScreen(game, levelData));
            //screen.dispose();

            int currentSelectedIndex = levelList.getSelectedIndex();

            if(currentSelectedIndex != selectedIndex)
            {
                selectedIndex = currentSelectedIndex;
                screen.setSelectedLevel(levelDatas[selectedIndex]);
            }

            return true;
        }
    }
}

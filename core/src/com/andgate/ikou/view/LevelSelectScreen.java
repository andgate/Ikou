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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private LevelData[] levels;

    private static final String SELECT_LEVEL_TEXT = "Select Level";

    public LevelSelectScreen(final Ikou game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        levels = LevelDatabaseService.getLevels();

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

        for(int i = 0; i < levels.length; i++)
        {
            final Table levelInfoTable = new Table();

            LevelData level = levels[i];

            final ShaderLabel levelNameLabel = new ShaderLabel(level.name, levelOptionLabelStyle, game.fontShader);

            final String levelCompletion = level.completedFloors + " out of " + level.totalFloors;
            final ShaderLabel levelProgressLabel = new ShaderLabel(levelCompletion, levelOptionLabelStyle, game.fontShader);

            levelInfoTable.addListener(new LevelOptionClickListener(game, this, level));

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
        private final LevelData level;

        public LevelOptionClickListener(Ikou game, LevelSelectScreen screen, LevelData level)
        {
            super();

            this.game = game;
            this.screen = screen;
            this.level = level;
        }

        @Override
        public void clicked(InputEvent event, float x, float y)
        {
            //game.buttonPressedSound.play();
            game.setScreen(new FloorSelectScreen(game, level));
            screen.dispose();
        }
    }
}

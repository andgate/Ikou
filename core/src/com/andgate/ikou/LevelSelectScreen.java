package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen implements Screen
{
    private static final String TAG = "LevelSelectScreen";

    private final Ikou game;
    private SpriteBatch batch;
    private Stage stage;

    private static final String SELECT_LEVEL_TEXT = "Select Level";

    public LevelSelectScreen(final Ikou newGame)
    {
        game = newGame;
        batch = new SpriteBatch();
        buildStage();
    }

    public void buildStage()
    {
        if(stage != null)
            stage.dispose();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        final Label.LabelStyle titleLabelStyle = new Label.LabelStyle(game.menuTitleFont, Color.CYAN);
        final Label titleLabel = new Label(SELECT_LEVEL_TEXT, titleLabelStyle);


        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(game.skin.getDrawable("default-round"),
                                                                                      game.skin.getDrawable("default-round-down"),
                                                                                      game.skin.getDrawable("default-round"),
                                                                                      game.menuOptionFont);


        final Label.LabelStyle levelOptionLabelStyle = new Label.LabelStyle(game.menuOptionFont, Color.BLACK);
        LevelData[] levels = LevelDatabaseService.getLevels();
        Table levelOptionsTable = new Table();

        for(int i = 0; i < levels.length; i++)
        {
            final Table levelInfoTable = new Table();

            LevelData level = levels[i];

            final Label levelNameLabel = new Label(level.name, levelOptionLabelStyle);

            final String levelCompletion = level.completedFloors + " out of " + level.totalFloors;
            final Label levelProgressLabel = new Label(levelCompletion, levelOptionLabelStyle);

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

        batch.begin();
        stage.draw();
        batch.end();

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
        if(batch != null)
            batch.dispose();
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
            /*try
            {
                game.setScreen(new GameScreen(game, level));
                screen.dispose();
            }
            catch(InvalidFileFormatException e)
            {
                Gdx.app.error(TAG, "Error loading level", e);
            }*/
        }
    }
}

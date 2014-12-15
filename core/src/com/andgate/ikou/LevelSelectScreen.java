package com.andgate.ikou;

import com.andgate.ikou.exception.InvalidFileFormatException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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

import java.util.ArrayList;

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

        Table levelsTable = new Table();

        String[] levelNames = getLevelNames();
        TextButton[] levelOptions = new TextButton[levelNames.length];

        for(int i = 0; i < levelNames.length; i++)
        {
            levelOptions[i] = new TextButton(levelNames[i], buttonStyle);

            final String levelName = levelNames[i];

            levelOptions[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //game.buttonPressedSound.play();
                    try
                    {
                        game.setScreen(new GameScreen(game, levelName));
                    }
                    catch(InvalidFileFormatException e)
                    {
                        Gdx.app.error(TAG, "Error loading level", e);
                    }

                    LevelSelectScreen.this.dispose();
                }
            });

            levelsTable.add(levelOptions[i]).row();
        }

        ScrollPane scrollPane = new ScrollPane(levelsTable, game.skin);

        Table table = new Table();

        table.add(titleLabel).expand().center().top().row();
        table.add(scrollPane).spaceBottom(20.0f).row();

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
            Gdx.app.exit();
        }
    }

    public String[] getLevelNames()
    {
        FileHandle dirHandle = Gdx.files.internal(Constants.LEVELS_DIRECTORY);

        ArrayList<String> levelNamesList = new ArrayList<String>();

        for(FileHandle entry : dirHandle.list())
        {
            String levelName = entry.nameWithoutExtension();
            levelNamesList.add(levelName);
        }

        return levelNamesList.toArray(new String[levelNamesList.size()]);
    }

    @Override
    public void resize(int width, int height) {
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
}

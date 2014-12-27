package com.andgate.ikou.view;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen
{
    private static final String TAG = "MainMenuScreen";

    private final Ikou game;
    private Stage stage;

    private static final String PLAY_BUTTON_TEXT = "Play";
    private static final String BUILD_BUTTON_TEXT = "Build";

    public MainMenuScreen(final Ikou game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        buildStage();
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        final LabelStyle titleLabelStyle = new LabelStyle(game.logoFont, Color.CYAN);
        final ShaderLabel titleLabel = new ShaderLabel(Constants.GAME_NAME, titleLabelStyle, game.fontShader);


        final TextButtonStyle buttonStyle = new TextButtonStyle(game.skin.getDrawable("default-round"),
                                                                game.skin.getDrawable("default-round-down"),
                                                                game.skin.getDrawable("default-round"),
                                                                game.menuOptionFont);

        final TextButton playButton = new TextButton(PLAY_BUTTON_TEXT, buttonStyle);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.buttonPressedSound.play();
                game.setScreen(new LevelSelectScreen(game));
                MainMenuScreen.this.dispose();
            }
        });

        final TextButton buildButton = new TextButton(BUILD_BUTTON_TEXT, buttonStyle);

        buildButton.addListener(new ClickListener() {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       //game.buttonPressedSound.play();
                                       game.setScreen(new LevelBuilderScreen(game));
                                       MainMenuScreen.this.dispose();
                                   }
                               });

        Table table = new Table();

        table.add(titleLabel).center().top().spaceBottom(25.0f).row();
        table.add(playButton).fill().spaceBottom(20.0f).center().bottom().row();
        table.add(buildButton).fill().spaceBottom(20.0f).center().bottom().row();

        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batch.begin();
        stage.draw();
        //batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.app.exit();
        }

        stage.act();
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
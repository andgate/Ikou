package com.andgate.ikou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Gabe on 12/18/2014.
 */
public class FloorSelectScreen extends ScreenAdapter
{
    private static final String TAG = "FloorSelectScreen";

    private final Ikou game;
    private SpriteBatch batch;
    private Stage stage;

    private final String levelName;

    private static final String SELECT_FLOOR_TEXT = "Select a Floor";

    public FloorSelectScreen(final Ikou newGame, String levelName)
    {
        game = newGame;
        batch = new SpriteBatch();
        this.levelName = levelName;

        buildStage();
    }

    public void buildStage()
    {
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

        stage.act();
    }

    public void gotoLevelSelect()
    {

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
}

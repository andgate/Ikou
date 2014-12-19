package com.andgate.ikou;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FloorSelectScreen extends ScreenAdapter
{
    private static final String TAG = "FloorSelectScreen";

    private final Ikou game;
    private SpriteBatch batch;
    private Stage stage;

    private final LevelData level;

    private static final String SELECT_FLOOR_TEXT = "Select a Floor";

    private static final int COLUMNS = 7;

    public FloorSelectScreen(final Ikou newGame, LevelData level)
    {
        game = newGame;
        batch = new SpriteBatch();
        this.level = level;

        buildStage();
    }

    public void buildStage()
    {
        if(stage != null)
            stage.dispose();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        final Label.LabelStyle titleLabelStyle = new Label.LabelStyle(game.menuTitleFont, Color.CYAN);
        final Label titleLabel = new Label(SELECT_FLOOR_TEXT, titleLabelStyle);

        final Label.LabelStyle floorOptionLabelStyle = new Label.LabelStyle(game.menuOptionFont, Color.BLACK);
        Table floorOptionsTable = new Table();


        float sidePadding = 1.0f * game.ppm;
        float actorLength = (float)Gdx.graphics.getWidth() / COLUMNS - sidePadding * 2.0f;

        for(int i = 0; i < level.totalFloors; i++)
        {
            int floorNumber = i + 1;
            final Label floorLabel = new Label("" + floorNumber, floorOptionLabelStyle);
            //floorLabel.addListener(new FloorOptionClickListener(game, this, level));

            floorOptionsTable.add(floorLabel).pad(sidePadding).width(actorLength).height(actorLength);

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

        batch.begin();
        stage.draw();
        batch.end();

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
        if(batch != null)
            batch.dispose();
    }
}

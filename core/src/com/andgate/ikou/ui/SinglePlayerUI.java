package com.andgate.ikou.ui;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;
import com.andgate.ikou.model.Level;

import com.andgate.ikou.model.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;

public class SinglePlayerUI implements Disposable
{
    private static final String TAG = "HelpScreen";
    private final Ikou game;
    private Level level;
    private Player player;

    public Stage stage;

    private Label depthLabel;
    private Label fpsLabel;


    private float game_ui_font_scale;
    private float debug_font_scale;

    public SinglePlayerUI(Ikou game, Level level, Player player)
    {
        this.game = game;
        stage = new Stage();
        this.level = level;
        this.player = player;

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
    }

    public void build()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        calc_font_size();

        final Label.LabelStyle uiLabelStyle = new Label.LabelStyle(game.arial_fnt, Color.BLACK);

        final String depthString = "" + player.getDepth();
        depthLabel = new Label(depthString, uiLabelStyle);
        depthLabel.setFontScale(game_ui_font_scale);

        fpsLabel = new Label("", uiLabelStyle);
        fpsLabel.setFontScale(debug_font_scale);

        final String seedString = "Seed: " + level.getSeed();
        final Label seedLabel = new Label(seedString, uiLabelStyle);
        seedLabel.setFontScale(debug_font_scale);

        Table infoTable = new Table();
        infoTable.add(depthLabel).expandX().row();
        if(game.debug) {
            infoTable.add(fpsLabel).left().row();
            infoTable.add(seedLabel).left().row();
        }
        infoTable.setBackground(game.whiteTransparentOverlay);

        Table table = new Table();
        table.setFillParent(true);
        table.top().left();
        table.add(infoTable).expandX().fillX();

        stage.addActor(table);
        //stage.setDebugAll(true);
    }

    public void update()
    {
        final String depthString = "" + (player.getDepth() + 1);
        depthLabel.setText(depthString);

        final String fpsString = "FPS: " + Gdx.graphics.getFramesPerSecond();
        fpsLabel.setText(fpsString);
    }

    void calc_font_size()
    {
        float font_scale_factor = game.ppu / (float)Constants.ARIAL_FONT_SIZE;
        game_ui_font_scale = Constants.GAME_UI_FONT_UNIT_SIZE * font_scale_factor;
        debug_font_scale = Constants.DEBUG_FONT_UNIT_SIZE * font_scale_factor;
    }

    @Override
    public void dispose()
    {
        if(stage != null)
            stage.dispose();
    }
}

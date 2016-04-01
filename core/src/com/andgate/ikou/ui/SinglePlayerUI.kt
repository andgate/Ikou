package com.andgate.ikou.ui;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;

import com.andgate.ikou.actor.MazeActor;
import com.andgate.ikou.actor.player.PlayerActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;

class SinglePlayerUI(val game: Ikou,
                     val maze: MazeActor,
                     val player: PlayerActor)
: Disposable
{
    private val TAG: String = "HelpScreen"

    val stage = Stage()

    private val uiLabelStyle = Label.LabelStyle(game.arial_fnt, Color.BLACK)
    private var depthLabel = Label("", uiLabelStyle)
    private var fpsLabel = Label("", uiLabelStyle)


    private var game_ui_font_scale: Float = 0f
    private var debug_font_scale: Float = 0f

    init
    {
        val bg = Constants.BACKGROUND_COLOR
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a)
    }

    fun build()
    {
        stage.clear()
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth().toFloat(), Gdx.graphics.getHeight().toFloat())
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true)

        calc_font_size()

        val depthString: String = "" + player.model.transform.getTranslation(Vector3()).y
        depthLabel = Label(depthString, uiLabelStyle)
        depthLabel.setText(depthString)
        depthLabel.setFontScale(game_ui_font_scale)

        fpsLabel.setFontScale(debug_font_scale)

        val seedString: String = "Seed: " + maze.seed_phrase
        val seedLabel = Label(seedString, uiLabelStyle)
        seedLabel.setFontScale(debug_font_scale)

        val infoTable = Table()
        infoTable.add(depthLabel).expandX().row()
        if(game.debug) {
            infoTable.add(fpsLabel).left().row()
            infoTable.add(seedLabel).left().row()
        }
        infoTable.setBackground(game.whiteTransparentOverlay)

        val table = Table()
        table.setFillParent(true)
        table.top().left()
        table.add(infoTable).expandX().fillX()

        stage.addActor(table)
        //stage.setDebugAll(true)
    }

    fun update()
    {
        val depthString: String = "" + (player.model.transform.getTranslation(Vector3()).y + 1)
        depthLabel.setText(depthString)

        val fpsString: String = "FPS: " + Gdx.graphics.getFramesPerSecond()
        fpsLabel.setText(fpsString)
    }

    private fun calc_font_size()
    {
        val font_scale_factor: Float = game.ppu / Constants.ARIAL_FONT_SIZE
        game_ui_font_scale = Constants.GAME_UI_FONT_UNIT_SIZE * font_scale_factor
        debug_font_scale = Constants.DEBUG_FONT_UNIT_SIZE * font_scale_factor
    }

    override fun dispose()
    {
        stage.dispose()
    }
}

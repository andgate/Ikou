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

import com.andgate.ikou.Ikou;
import com.andgate.ikou.io.LevelDatabaseService;
import com.andgate.ikou.io.LevelLoader;
import com.andgate.ikou.io.LevelService;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LevelBuilderScreen implements Screen
{
    private final Ikou game;
    private Stage stage;
    private Level level;

    public LevelBuilderScreen(Ikou game)
    {
        this(game, new Level());
    }

    public LevelBuilderScreen(Ikou game, Level level)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        this.level = level;

        buildStage();

        LevelData[] levelDatas = LevelDatabaseService.getOldLevels();
        Level[] levels = new Level[levelDatas.length];

        for(int i = 0; i < levelDatas.length; i++)
        {
            try
            {
                levels[i] = LevelLoader.load(levelDatas[i]);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        for(Level newLevel : levels)
        {
            LevelService.write(newLevel);
        }
    }

    public void buildStage()
    {
        stage.clear();
        stage.getViewport().setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
}

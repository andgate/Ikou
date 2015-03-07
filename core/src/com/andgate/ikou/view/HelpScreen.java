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
import com.andgate.ikou.input.HelpScreenControllerListener;
import com.andgate.ikou.input.HelpScreenInputListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class HelpScreen extends ScreenAdapter
{
    private static final String TAG = "HelpScreen";

    private final Ikou game;
    private Stage stage;

    private enum State
    { Start, Play, End }
    private State state = State.Start;

    private HelpScreenControllerListener helpScreenControllerListener;

    public HelpScreen(Ikou game)
    {
        this.game = game;
        stage = new Stage();

        Color bg = Constants.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);

        Gdx.input.setInputProcessor(new HelpScreenInputListener(this));
        helpScreenControllerListener = new HelpScreenControllerListener(this);
        Controllers.addListener(helpScreenControllerListener);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

        update(delta);
    }

    private void update(float delta)
    {
        switch(state)
        {
            case Start:
                updateStart(delta);
                break;
            case Play:
                updatePlay(delta);
                break;
            case End:
                updateEnd(delta);
                break;
            default:
                break;
        }
    }

    private void updateStart(float delta)
    {
        state = State.Play;
    }

    private void updatePlay(float delta)
    {
        stage.act();
    }

    private void updateEnd(float delta)
    {
        game.setScreen(new MainMenuScreen(game));
        this.dispose();
    }

    public void end()
    {
        state = State.End;
    }

    @Override
    public void dispose()
    {
        if(helpScreenControllerListener != null)
            Controllers.removeListener(helpScreenControllerListener);
        if(stage != null)
            stage.dispose();
    }
}

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

package com.andgate.ikou.input;

import com.andgate.ikou.view.MainMenuScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuButtonClickListener extends ClickListener
{
    private final MainMenuScreen screen;
    private final MainMenuScreen.Option option;

    public MainMenuButtonClickListener(MainMenuScreen screen, MainMenuScreen.Option option)
    {
        this.screen = screen;
        this.option = option;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        screen.setOption(option);
        screen.end();
    }

    @Override
    public boolean keyDown (InputEvent event, int keycode) {
        if(keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE)
        {
            screen.setOption(option);
            screen.end();
        }
        return false;
    }

    @Override
    public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor)
    {
        super.enter(event, x, y, pointer, fromActor);
        screen.setSelection(option);
    }

    @Override
    public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        screen.cancelSelection();
    }
}

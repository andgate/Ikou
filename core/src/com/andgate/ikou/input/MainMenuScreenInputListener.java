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
import com.badlogic.gdx.InputAdapter;

public class MainMenuScreenInputListener extends InputAdapter
{
    private static final String TAG = "MainMenuScreenInputListener";
    private final MainMenuScreen screen;

    public MainMenuScreenInputListener(MainMenuScreen screen)
    {
        this.screen = screen;
    }

    public boolean keyDown (int keycode)
    {
        if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE)
        {
            screen.setOption(MainMenuScreen.Option.None);
            screen.end();
        }
        else if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            screen.selectNextOption();
        }
        else if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            screen.selectPreviousOption();
        }

        return false;
    }
}

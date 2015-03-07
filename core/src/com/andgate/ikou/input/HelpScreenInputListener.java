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

import com.andgate.ikou.view.HelpScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class HelpScreenInputListener extends InputAdapter
{
    private static final String TAG = "HelpScreenInputListener";
    private final HelpScreen screen;

    public HelpScreenInputListener(HelpScreen screen)
    {
        this.screen = screen;
    }

    public boolean keyDown (int keycode)
    {
        if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE)
        {
            screen.end();
        }

        return false;
    }

    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        screen.end();
        return false;
    }
}

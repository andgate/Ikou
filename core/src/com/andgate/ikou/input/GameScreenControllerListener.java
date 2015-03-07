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

import com.andgate.ikou.input.mappings.OuyaPad;
import com.andgate.ikou.input.mappings.Xbox360Pad;
import com.andgate.ikou.view.GameScreen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class GameScreenControllerListener extends ControllerAdapter
{
    private static final String TAG = "GameScreenControllerListener";

    private final GameScreen screen;
    private int startButton = -1;

    public GameScreenControllerListener(GameScreen screen)
    {
        this.screen = screen;
    }

    @Override
    public boolean buttonDown (Controller controller, int buttonIndex)
    {
        mapToController(controller);
        if(startButton == buttonIndex)
        {
            screen.endGame();
        }

        return false;
    }

    private void mapToController(Controller controller)
    {
        if(Xbox360Pad.isXbox360Controller(controller))
        {
            startButton = Xbox360Pad.BUTTON_START;
        }
        else if(OuyaPad.isOuyaController(controller))
        {
            startButton = OuyaPad.BUTTON_MENU;
        }
        else
        {
            startButton = -1;
        }
    }
}

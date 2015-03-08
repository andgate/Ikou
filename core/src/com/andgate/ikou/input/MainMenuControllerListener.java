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
import com.andgate.ikou.view.MainMenuScreen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class MainMenuControllerListener extends ControllerAdapter
{
    private static final String TAG = "MainMenuControllerListener";

    private final MainMenuScreen screen;
    private int startButton = -1;
    private int okayButton = -1;
    private int yAxisLeft = -1;

    public MainMenuControllerListener(MainMenuScreen screen)
    {
        this.screen = screen;
    }

    @Override
    public boolean buttonDown (Controller controller, int buttonIndex)
    {
        mapToController(controller);
        if(startButton == buttonIndex)
        {
            screen.setOption(MainMenuScreen.Option.None);
            screen.end();
        }
        else if(okayButton == buttonIndex)
        {
            screen.confirmSelection();
        }

        return false;
    }

    private boolean justSelected;

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        mapToController(controller);


        if(Math.abs(value) < 1.0 )
        {
            if(Math.abs(value) < 0.1)
                justSelected = false;
            return false;
        }

        if(axisIndex == yAxisLeft && !justSelected)
        {
            if(value < 0)
            {
                screen.selectPreviousOption();
            }
            else if(value > 0)
            {
                screen.selectNextOption();
            }
            justSelected = true;
        }

        return false;
    }

    private void mapToController(Controller controller)
    {
        if(Xbox360Pad.isXbox360Controller(controller))
        {
            startButton = Xbox360Pad.BUTTON_START;
            okayButton = Xbox360Pad.BUTTON_A;
            yAxisLeft = Xbox360Pad.AXIS_LEFT_Y;
        }
        else if(OuyaPad.isOuyaController(controller))
        {
            startButton = OuyaPad.BUTTON_MENU;
            okayButton = OuyaPad.BUTTON_O;
            yAxisLeft = OuyaPad.AXIS_LEFT_Y;
        }
        else
        {
            startButton = -1;
        }
    }
}

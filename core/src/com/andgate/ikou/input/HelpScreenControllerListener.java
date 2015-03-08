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
import com.andgate.ikou.view.HelpScreen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

public class HelpScreenControllerListener extends ControllerAdapter
{
    private static final String TAG = "HelpScreenControllerListener";

    private final HelpScreen screen;
    private int startButton = -1;
    private int cancelButton = -1;

    private int yAxisLeft = -1;
    private int yAxisRight = -1;

    public HelpScreenControllerListener(HelpScreen screen)
    {
        this.screen = screen;
    }

    @Override
    public boolean buttonDown (Controller controller, int buttonIndex)
    {
        mapToController(controller);
        if(startButton == buttonIndex || cancelButton == buttonIndex)
        {
            screen.end();
        }

        return false;
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        mapToController(controller);

        if(Math.abs(value) < 1.0f)
        {
            return false;
        }

        if(axisIndex == yAxisLeft || axisIndex == yAxisLeft)
        {
            if(value > 0)
            {
                screen.scroll(1.0f);
            }
            else if(value < 0)
            {
                screen.scroll(0.0f);
            }
        }

        return false;
    }

    private void mapToController(Controller controller)
    {
        if(Xbox360Pad.isXbox360Controller(controller))
        {
            startButton = Xbox360Pad.BUTTON_START;
            cancelButton = Xbox360Pad.BUTTON_B;
            yAxisLeft = Xbox360Pad.AXIS_LEFT_Y;
            yAxisRight = Xbox360Pad.AXIS_RIGHT_Y;
        }
        else if(OuyaPad.isOuyaController(controller))
        {
            startButton = OuyaPad.BUTTON_MENU;
            cancelButton = OuyaPad.BUTTON_A;
            yAxisLeft = OuyaPad.AXIS_LEFT_Y;
            yAxisRight = OuyaPad.AXIS_RIGHT_Y;
        }
        else
        {
            startButton = -1;
        }
    }
}

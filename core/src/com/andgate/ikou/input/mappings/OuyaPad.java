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

package com.andgate.ikou.input.mappings;

import com.badlogic.gdx.controllers.Controller;

public class OuyaPad
{
    public static final String ID = "OUYA Game Controller";
    public static final int BUTTON_O = 96;
    public static final int BUTTON_U = 99;
    public static final int BUTTON_Y = 100;
    public static final int BUTTON_A = 97;
    public static final int BUTTON_MENU = 82;
    public static final int BUTTON_DPAD_UP = 19;
    public static final int BUTTON_DPAD_DOWN = 20;
    public static final int BUTTON_DPAD_RIGHT = 22;
    public static final int BUTTON_DPAD_LEFT = 21;
    public static final int BUTTON_L1 = 104;
    public static final int BUTTON_L2 = 102;
    public static final int BUTTON_L3 = 106;
    public static final int BUTTON_R1 = 105;
    public static final int BUTTON_R2 = 103;
    public static final int BUTTON_R3 = 107;
    public static final int AXIS_LEFT_X = 0;
    public static final int AXIS_LEFT_Y = 1;
    public static final int AXIS_LEFT_TRIGGER = 2;
    public static final int AXIS_RIGHT_X = 3;
    public static final int AXIS_RIGHT_Y = 4;
    public static final int AXIS_RIGHT_TRIGGER = 5;
    public static final float STICK_DEADZONE = 0.25F;

    public static boolean isOuyaController(Controller controller)
    {
        return controller.getName().toLowerCase().contains("ouya") || controller.getName().toLowerCase().contains("cardhu");
    }
}

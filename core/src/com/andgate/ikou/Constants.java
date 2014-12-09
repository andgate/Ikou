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

package com.andgate.ikou;

public class Constants
{
    public static final float TIME_STEP = 1.0f / 60.0f;

    public static final int WORLD_LENGTH = 5;
    public static final float BUTTON_LENGTH = 0.7f;

    public static final String CAMERA_ICON_LOCATION
            = "data/icons/camera.png";
    public static final String CAMERA_ICON_DOWN_LOCATION
            = "data/icons/camera_down.png";
    public static final String MOVE_ICON_LOCATION
            = "data/icons/move.png";
    public static final String MOVE_ICON_DOWN_LOCATION
            = "data/icons/move_down.png";

    public static final float LITTLE_EPSILON = 1.0f / 1E10f;
    public static final float EPSILON = 1.0f / 1E7f;
    public static final float BIG_EPSILON = 1.0f / 1E3f;

    public static final float TILE_LENGTH = 1.0f;
    public static final float TILE_THICKNESS = TILE_LENGTH / 50.0f;

    /*public static final float LIGHT_THICKNESS = 0.05f;
    public static final float MEDIUM_THICKNESS = 0.1f;
    public static final float HEAVY_THICKNESS = 0.2f;*/
}

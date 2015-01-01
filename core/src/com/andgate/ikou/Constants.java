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
    public static final String GAME_NAME = "Ikou";

    public static final float TIME_STEP = 1.0f / 60.0f;

    public static final int WORLD_LENGTH = 30;
    public static final float BUTTON_LENGTH = 3.0f;

    public static final String SKIN_LOCATION
            = "data/ui/uiskin.json";

    public static final String SHADER_FOLDER
            = "data/shader/";
    public static final String FONT_VERT_SHADER
            = SHADER_FOLDER + "font.vert.glsl";
    public static final String FONT_FRAG_SHADER
            = SHADER_FOLDER + "font.frag.glsl";

    public static final String FONTS_FOLDER
            = "data/fonts/";

    private static final String FNT_EXTENSION
            = ".fnt";
    private static final String PNG_EXTENSION
            = ".png";

    private static final String LOGO_FONT_NAME
            = FONTS_FOLDER + "arial";
    private static final String MENU_FONT_NAME
            = FONTS_FOLDER + "arial";

    public static final String LOGO_FONT_FNT
            = LOGO_FONT_NAME + FNT_EXTENSION;
    public static final String LOGO_FONT_PNG
            = LOGO_FONT_NAME + PNG_EXTENSION;

    public static final String MENU_FONT_FNT
            = MENU_FONT_NAME + FNT_EXTENSION;
    public static final String MENU_FONT_PNG
            = MENU_FONT_NAME + PNG_EXTENSION;

    public static final int LOGO_FONT_SIZE = 7;
    public static final int MENU_TITLE_FONT_SIZE = 3;
    public static final int MENU_OPTION_FONT_SIZE = 2;

    public static final String CAMERA_ICON_LOCATION
            = "data/icons/camera.png";
    public static final String CAMERA_ICON_DOWN_LOCATION
            = "data/icons/camera_down.png";
    public static final String MOVE_ICON_LOCATION
            = "data/icons/move.png";
    public static final String MOVE_ICON_DOWN_LOCATION
            = "data/icons/move_down.png";

    public static final String LEVELS_INTERNAL_PATH
            = "data/levels/";
    public static final String LEVELS_EXTERNAL_PATH
            = "Ikou/levels/";
    public static final String FLOOR_EXTENSION
            = ".txt";

    public static final String LEVEL_EXTENSION_NO_DOT
            = "lvl";
    public static final String LEVEL_EXTENSION
            = "." + LEVEL_EXTENSION_NO_DOT;

    public static final String TEMP_EXTENSION_NO_DOT
            = "tmp";
    public static final String TEMP_EXTENSION
            = "." + TEMP_EXTENSION_NO_DOT;

    public static final String PROGRESS_DATABASE_EXTERNAL_PATH
            = "Ikou/progress.data";

    public static final float LITTLE_EPSILON = 1.0f / 1E10f;
    public static final float EPSILON = 1.0f / 1E7f;
    public static final float BIG_EPSILON = 1.0f / 1E3f;

    public static final float TILE_LENGTH = 1.0f;
    public static final float TILE_THICKNESS = TILE_LENGTH / 50.0f;

    public static final float FLOOR_SPACING = 10.0f;

    public static final float DEFAULT_FIELD_OF_VIEW =  67f;
    public static final float CAMERA_FAR = 40.0f;
    public static final float CAMERA_DISTANCE = 5.0f;
    public static final float CAMERA_ANGLE_TO_PLAYER = 45.0f;
    public static final float CAMERA_VERTICAL_DISTANCE = (float)Math.sin(CAMERA_ANGLE_TO_PLAYER) * CAMERA_DISTANCE;
    public static final float CAMERA_HORIZONTAL_DISTANCE = (float)Math.cos(CAMERA_ANGLE_TO_PLAYER) * CAMERA_DISTANCE;
}

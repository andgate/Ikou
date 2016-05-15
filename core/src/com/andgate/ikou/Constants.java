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

import com.badlogic.gdx.graphics.Color;

public class Constants
{
    public static final String GAME_NAME = "Ikou";

    public static final float TIME_STEP = 1.0f / 60.0f;

    public static final int WORLD_LENGTH = 30;
    public static final float BUTTON_LENGTH = 3.0f;

    public static final Color BACKGROUND_COLOR = new Color(Color.WHITE);

    public static final String SOUND_FOLDER
            = "data/sound/";

    public static final String SHADER_FOLDER
            = "data/shader/";
    public static final String FONT_VERT_SHADER
            = SHADER_FOLDER + "font.vert.glsl";
    public static final String FONT_FRAG_SHADER
            = SHADER_FOLDER + "font.frag.glsl";

    public static final String FONTS_FOLDER
            = "data/fonts/";

    private static final String TTF_EXTENSION
            = ".ttf";
    private static final String FNT_EXTENSION
            = ".fnt";

    private static String ttf_path(String name)
    {
        return FONTS_FOLDER + name + TTF_EXTENSION;
    }
    private static String fnt_path(String name) { return FONTS_FOLDER + name + "/" + name + FNT_EXTENSION; }

    public static final String ARIAL_FONT_PATH
            = ttf_path("arial");
    public static final int ARIAL_FONT_SIZE
            = 200;

    public static final String LOGO_FONT
            = ttf_path("logo");
    public static final String MENU_TITLE_FONT
            = ttf_path("menu-title");
    public static final String MENU_OPTION_FONT
            = ttf_path("menu-option");
    public static final String HELP_FONT
            = ttf_path("help");

    public static final float LOGO_FONT_UNIT_SIZE = 9.0f;
    public static final float MENU_TITLE_FONT_UNIT_SIZE = 4.0f;
    public static final float MENU_OPTION_FONT_UNIT_SIZE = 3.0f;
    public static final float HELP_FONT_UNIT_SIZE = 2.0f;
    public static final float GAME_UI_FONT_UNIT_SIZE = 3.0f;
    public static final float DEBUG_FONT_UNIT_SIZE = 2.0f;

    public static final Color LOGO_FONT_COLOR = new Color(0.215f, 0.647f, 1.0f, 0.5f);

    public static final float LITTLE_EPSILON = 1.0f / 1E10f;
    public static final float EPSILON = 1.0f / 1E7f;
    public static final float BIG_EPSILON = 1.0f / 1E3f;

    public static final float DEFAULT_FIELD_OF_VIEW = 67f;
    public static final float CAMERA_FAR = 40.0f;
    public static final float CAMERA_DISTANCE = 8.0f;
    public static final float CAMERA_ANGLE_TO_PLAYER = 45.0f;
    public static final float CAMERA_VERTICAL_DISTANCE = (float)Math.sin(Math.toRadians(CAMERA_ANGLE_TO_PLAYER)) * CAMERA_DISTANCE;
    public static final float CAMERA_HORIZONTAL_DISTANCE = (float)Math.cos(Math.toRadians(CAMERA_ANGLE_TO_PLAYER)) * CAMERA_DISTANCE;

    public static final String PLAYER_PREFS = "Ikou Player Preferences";
    public static final String PLAYER_PREF_X = "x coord";
    public static final String PLAYER_PREF_Y = "y coord";
    public static final String PLAYER_PREF_Z = "z coord";
    public static final String PLAYER_PREF_DEPTH = "depth";
    public static final String PLAYER_PREF_LEVEL_SEED = "level seed";

    public static final String HELP_TEXT = "data/text/help.txt";

    public static final long RESERVED_SEED = -1;

    public static final int DEFAULT_DEPTH = 0;
}

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

package com.andgate.ikou.io;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class LevelService
{
    private static final String TAG = "LevelService";

    public static Level read(String name)
    {
        FileHandle levelFile = Gdx.files.external(Constants.LEVELS_INTERNAL_PATH + name + Constants.LEVEL_EXTENSION);

        if(levelFile.exists())
        {
            String jsonString;
            try
            {
                jsonString = Base64Coder.decodeString(levelFile.readString());
            }
            catch (java.lang.IllegalArgumentException e)
            {
                // The highscore has been tampered with,
                // so now they get a new one.
                return new Level();
            }

            Json json = new Json();

            Level level = json.fromJson(Level.class, jsonString);
            if(level != null)
            {
                return level;
            }
        }

        // Always return a fresh db,
        // if something is mucked up.
        // Only executes if something is wrong
        // with the
        return new Level();
    }

    public static void write(Level level)
    {
        Json json = new Json();
        String jsonString = json.toJson(level);
        String encodedString = Base64Coder.encodeString(jsonString);
        FileHandle levelFile = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH + level.getName() + Constants.LEVEL_EXTENSION);
        levelFile.writeString(encodedString, false);
    }
}

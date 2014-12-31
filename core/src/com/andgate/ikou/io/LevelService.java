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
import com.badlogic.gdx.utils.JsonWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class LevelService
{
    private static final String TAG = "LevelService";

    public static Level readInternal(String filename)
    {
        FileHandle levelFile = Gdx.files.internal(filename);
        return read(levelFile);
    }

    public static Level readExternal(String filename)
    {
        FileHandle levelFile = Gdx.files.external(filename);
        return read(levelFile);
    }

    public static Level read(FileHandle levelFile)
    {
        if(levelFile.exists() && levelFile.extension().equals(Constants.LEVEL_EXTENSION_NO_DOT))
        {
            // TODO: use a buffered input to stream in the level data :)
            String jsonString = levelFile.readString();
            Gson gson = new Gson();
            Level level = gson.fromJson(jsonString, Level.class);

            if(level != null)
            {
                return level;
            }
        }

        // Always return a fresh db,
        // if something is mucked up.
        // Only executes if something is wrong
        // with the json file
        return new Level();
    }

    // see http://stackoverflow.com/questions/10765831/out-of-memory-exception-in-gson-fromjson
    public static void write(Level level)
    {
        // Shrinking causes json to write output forever.
        // FIXME: Compress the level :)
        //level.shrink();

        FileHandle levelFile = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH + level.getName() + Constants.LEVEL_EXTENSION);
        OutputStream os = levelFile.write(false);
        Writer writer = new BufferedWriter(new OutputStreamWriter(os));

        Gson gson = new Gson();
        JsonWriter w = new JsonWriter(writer);
        gson.toJson(level, w);

        try
        {
            w.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
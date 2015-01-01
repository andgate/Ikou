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
import com.badlogic.gdx.utils.compression.Lzma;
import com.gc.iotools.stream.is.InputStreamFromOutputStream;
import com.gc.iotools.stream.os.OutputStreamToInputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class LevelService
{
    private static final String TAG = "LevelService";

    public static Level read(final FileHandle levelFile)
            throws IOException
    {
        Level level = null;

        if(levelFile.exists() && levelFile.extension().equals(Constants.LEVEL_EXTENSION_NO_DOT))
        {
            InputStream levelIn = new GZIPInputStream(levelFile.read());
            Reader reader = new BufferedReader(new InputStreamReader(levelIn));
            JsonReader jsonReader = new JsonReader(reader);

            try
            {
                // Skip the first int, it's just the floor numbers.
                levelIn.read();
                Gson gson = new Gson();
                level = gson.fromJson(jsonReader, Level.class);
            }
            finally
            {
                jsonReader.close();
            }
        }

        if(level == null)
        {
            final String errorMessage = "Failed to load level \"" + levelFile.path() + levelFile.name() + "\"";
            throw new IOException(errorMessage);
        }

        return level;
    }

    public static void write(final Level level)
        throws IOException
    {
        // Shrinking causes json to write output forever.
        // FIXME: Compress the level :)
        //level.shrink();

        String levelFileName = level.getName() + Constants.LEVEL_EXTENSION;
        FileHandle levelFile = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH + levelFileName);

        OutputStream levelOut = new GZIPOutputStream(levelFile.write(false));
        Writer tmpWriter = new BufferedWriter(new OutputStreamWriter(levelOut));
        JsonWriter jsonWriter = new JsonWriter(tmpWriter);

        try
        {
            levelOut.write(level.getFloors().length);
            Gson gson = new Gson();
            gson.toJson(level, jsonWriter);
        }
        finally
        {
            jsonWriter.close();
        }
    }
}
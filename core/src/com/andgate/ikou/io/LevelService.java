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

    public static Level read(final FileHandle levelFile)
    {
        Level level = null;

        if(levelFile.exists() && levelFile.extension().equals(Constants.LEVEL_EXTENSION_NO_DOT))
        {
            String tempFilename = levelFile.name() + Constants.TEMP_EXTENSION;

            FileHandle tmpFile = Gdx.files.local(tempFilename);

            InputStream levelIn = levelFile.read();
            OutputStream tmpOut = tmpFile.write(false);

            try
            {
                Lzma.decompress(levelIn, tmpOut);
            }
            catch(IOException e)
            {
                final String errorMessage = "Error decompressing level.";
                Gdx.app.error(TAG, errorMessage, e);
            }
            finally
            {
                try
                {
                    levelIn.close();
                    tmpOut.close();
                }
                catch(IOException e)
                {
                    final String errorMessage = "Error closing level compression files.";
                    Gdx.app.error(TAG, errorMessage, e);
                }
            }

            InputStream tmpIn = tmpFile.read();
            Reader reader = new BufferedReader(new InputStreamReader(tmpIn));
            JsonReader jsonReader = new JsonReader(reader);

            try
            {
                Gson gson = new Gson();
                level = gson.fromJson(jsonReader, Level.class);
            }
            finally
            {
                try
                {
                    tmpIn.close();
                    jsonReader.close();

                    tmpFile.delete();
                }
                catch(final Exception e)
                {
                    final String errorMessage = "Error closing temporary level file.";
                    Gdx.app.error(TAG, errorMessage, e);
                }
            }
        }

        // Return the loaded level,
        // otherwise, return a new level.
        if(level != null)
        {
            return level;
        }

        return new Level();
    }

    // see http://stackoverflow.com/questions/10765831/out-of-memory-exception-in-gson-fromjson
    public static void write(final Level level)
    {
        String levelFileName = level.getName() + Constants.LEVEL_EXTENSION;
        String tempFileName = levelFileName + Constants.TEMP_EXTENSION;
        // Shrinking causes json to write output forever.
        // FIXME: Compress the level :)
        //level.shrink();

        FileHandle tmpFile = Gdx.files.local(tempFileName);

        OutputStream tmpOut = tmpFile.write(false);
        Writer tmpWriter = new BufferedWriter(new OutputStreamWriter(tmpOut));
        JsonWriter jsonWriter = new JsonWriter(tmpWriter);

        try
        {
            Gson gson = new Gson();
            gson.toJson(level, jsonWriter);
        }
        finally
        {
            try
            {
                jsonWriter.close();
            }
            catch(final IOException e)
            {
                final String errorMessage = "Error cleaning up intermediate files.";
                Gdx.app.error(TAG, errorMessage, e);
            }
        }

        FileHandle levelFile = Gdx.files.external(Constants.LEVELS_EXTERNAL_PATH + levelFileName);

        InputStream tmpIn = tmpFile.read();
        OutputStream levelOut = levelFile.write(false);

        try
        {
            Lzma.compress(tmpIn, levelOut);
        }
        catch(final IOException e)
        {
            final String errorMessage = "Error compressing level file.";
            Gdx.app.error(TAG, errorMessage, e);
        }
        finally
        {
            try
            {
                tmpIn.close();
                levelOut.close();

                tmpFile.delete();
            }
            catch(Exception e)
            {
                final String errorMessage = "Error writing level files.";
                Gdx.app.error(TAG, errorMessage, e);
            }
        }
    }
}
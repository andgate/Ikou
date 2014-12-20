package com.andgate.ikou.io;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.ProgressDatabase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class ProgressDatabaseService
{
    private static final String TAG = "ProgressDatabaseService";

    public static ProgressDatabase read()
    {
        FileHandle saveFile = Gdx.files.external(Constants.PROGRESS_DATABASE_EXTERNAL_PATH);

        if(saveFile.exists())
        {
            String jsonString;
            try
            {
                jsonString = Base64Coder.decodeString(saveFile.readString());
            }
            catch (java.lang.IllegalArgumentException e)
            {
                // The highscore has been tampered with,
                // so now they get a new one.
                return new ProgressDatabase();
            }

            Json json = new Json();

            ProgressDatabase progressDatabase = json.fromJson(ProgressDatabase.class, jsonString);
            if(progressDatabase != null)
            {
                return progressDatabase;
            }
        }

        // Always return a fresh db,
        // if something is mucked up.
        // Only executes if something is wrong
        // with the
        return new ProgressDatabase();
    }

    public static void write(ProgressDatabase progressDatabase)
    {
        Json json = new Json();
        String jsonString = json.toJson(progressDatabase);
        String encodedString = Base64Coder.encodeString(jsonString);
        FileHandle saveFile = Gdx.files.external(Constants.PROGRESS_DATABASE_EXTERNAL_PATH);
        saveFile.writeString(encodedString, false);
    }
}

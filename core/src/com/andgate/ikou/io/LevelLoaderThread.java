package com.andgate.ikou.io;

import com.andgate.ikou.Ikou;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.render.LevelRender;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Disposable;

import java.io.IOException;

public class LevelLoaderThread extends Thread implements Disposable
{
    private static final String TAG = "LevelLoaderThread";

    private Ikou game;
    private LevelData levelData;
    private Level level = null;
    private LevelRender levelRender = null;

    private boolean success = true;

    public LevelLoaderThread(Ikou game, LevelData levelData)
    {
        this.game = game;
        this.levelData = levelData;
    }

    @Override
    public void run()
    {
        try
        {
            level = LevelLoader.load(levelData);
            levelRender = new LevelRender(level);
        }
        catch(IOException e)
        {
            success = false;
        }
    }

    public boolean isSuccessful()
    {
        return success;
    }

    public Level getLevel()
    {
        if(this.isAlive())
        {
            return null;
        }

        return level;
    }

    public LevelRender getLevelRender()
    {
        if(this.isAlive())
        {
            return null;
        }

        return levelRender;
    }

    @Override
    public void dispose()
    {
        levelRender.dispose();
    }
}

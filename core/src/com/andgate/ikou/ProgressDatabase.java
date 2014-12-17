package com.andgate.ikou;

import com.badlogic.gdx.utils.Disposable;

import java.util.Hashtable;

public class ProgressDatabase
{
    private static final String TAG = "ProgressDatabase";

    private final Hashtable<String, Integer> progressTable;

    public ProgressDatabase()
    {
        this.progressTable = new Hashtable<String, Integer>();
    }

    public ProgressDatabase(Hashtable<String, Integer> levelTable)
    {
        this.progressTable = levelTable;
    }

    public int getFloorsVisited(String levelName)
    {
        return progressTable.get(levelName);
    }

    public void setFloorsVisited(String levelName, int floorsVisited)
    {
        progressTable.put(levelName, new Integer(floorsVisited));
    }
}

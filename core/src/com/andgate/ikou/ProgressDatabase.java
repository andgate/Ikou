package com.andgate.ikou;

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
        if(progressTable.containsKey(levelName))
        {
            return progressTable.get(levelName);
        }

        return 0;
    }

    public void setFloorsVisited(String levelName, int floorsVisited)
    {
        progressTable.put(levelName, new Integer(floorsVisited));
    }
}
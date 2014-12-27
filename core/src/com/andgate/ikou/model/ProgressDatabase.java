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

package com.andgate.ikou.model;

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

    public int getFloorsCompleted(String levelName)
    {
        if(progressTable.containsKey(levelName))
        {
            return progressTable.get(levelName);
        }

        return 0;
    }

    public void setFloorsCompleted(String levelName, int floorsVisited)
    {
        progressTable.put(levelName, new Integer(floorsVisited));
    }
}
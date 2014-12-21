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

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.tile.TileCode;
import com.andgate.ikou.utility.Vector2i;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TileMaze
{
    private static final String TAG = "TileMaze";

    public static final Vector2i UP = new Vector2i(0, 1);
    public static final Vector2i DOWN = new Vector2i(0, -1);
    public static final Vector2i LEFT = new Vector2i(1, 0);
    public static final Vector2i RIGHT = new Vector2i(-1, 0);
    public static final Vector2i NONE = new Vector2i(0, 0);

    private char[][] tiles;
    private final Vector2i startPosition;
    private final Vector2i endPosition;
    private Vector2i playerPosition;
    private ArrayList<WinListener> winListeners = new ArrayList<>();
    private ArrayList<PlayerMoveListener> playerMoveListeners = new ArrayList<>();

    public TileMaze(char[][] tiles, Vector2i startPosition, Vector2i endPosition)
    {
        this.tiles = tiles;
        this.startPosition = new Vector2i(startPosition);
        this.playerPosition = new Vector2i(startPosition);
        this.endPosition = new Vector2i(endPosition);
    }

    public void addWinListener(WinListener winListener)
    {
        winListeners.add(winListener);
    }

    public void removeWinListener(WinListener winListener)
    {
        winListeners.remove(winListener);
    }

    public void notifyWinListeners()
    {
        for(WinListener winListener : winListeners)
        {
            winListener.mazeWon();
        }
    }

    public void addPlayerMoveListener(PlayerMoveListener playerMoveListener)
    {
        playerMoveListeners.add(playerMoveListener);
    }

    public void removePlayerMoveListener(PlayerMoveListener playerMoveListener)
    {
        playerMoveListeners.remove(playerMoveListener);
    }

    public void notifyPlayerMoveListeners(int dx, int dy)
    {
        for(PlayerMoveListener playerMoveListener : playerMoveListeners)
        {
            playerMoveListener.movePlayerBy(dx, dy);
        }
    }

    public char[][] getTiles()
    {
        return tiles;
    }

    public Vector2i getStartPosition()
    {
        return startPosition;
    }

    public Vector2i getEndPosition()
    {
        return endPosition;
    }

    public Vector2i getPlayerPosition()
    {
        return playerPosition;
    }

    public char getTile(int x, int y)
    {
        if( (y < tiles.length)
         && (x < tiles[y].length))
        {
            return tiles[y][x];
        }
        else
        {
            return ' ';
        }
    }

    public int getTileCount()
    {
        int tileCount = 0;

        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[i].length; j++)
            {
                if(tiles[i][j] != ' ')
                {
                    tileCount++;
                }
            }
        }
        return tileCount;
    }

    Vector2i accumMoveDelta = new Vector2i();
    Vector2i prevAccumMoveDelta = new Vector2i();
    public void move(Vector2i velocity)
    {
        accumMoveDelta.set(0, 0);

        do
        {
            prevAccumMoveDelta.set(accumMoveDelta);
            accumMoveDelta.add(step(velocity));
        }
        while(!accumMoveDelta.equals(prevAccumMoveDelta)
                && !isStopped);

        isStopped = false;

        notifyPlayerMoveListeners(accumMoveDelta.x, accumMoveDelta.y);
    }

    Vector2i moveDelta = new Vector2i();
    private boolean isStopped = false;
    private Vector2i step(Vector2i velocity)
    {
        int x = playerPosition.x + velocity.x;
        int y = playerPosition.y + velocity.y;

        moveDelta.set(0, 0);

        // Is the next position within the map bounds?
        if( (0 <= y && y < tiles.length)
         && (0 <= x && x < tiles[y].length))
        {
            // Use the next tile to decide
            // what to do.
            char nextTile = tiles[y][x];
            switch(nextTile)
            {
                case TileCode.SMOOTH_TILE:
                    moveDelta.set(velocity);
                    playerPosition.add(moveDelta);
                    break;
                case TileCode.ROUGH_TILE:
                    moveDelta.set(velocity);
                    playerPosition.add(moveDelta);
                    isStopped = true;
                    break;
                case TileCode.OBSTACLE_TILE:
                    isStopped = true;
                    break;
                case TileCode.END_TILE:
                    moveDelta.set(velocity);
                    playerPosition.add(moveDelta);
                    notifyWinListeners();
                    break;
                default:
                    break;
            }
        }

        return moveDelta;
    }

    @Override
    public String toString()
    {
        String out = "";

        out += "Player position: ";
        out += playerPosition.toString();
        out += '\n';

        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[i].length; j++)
            {
                out += tiles[i][j];
            }

            out += '\n';
        }

        return out;
    }

    public interface WinListener
    {
        public void mazeWon();
    }

    public interface PlayerMoveListener
    {
        public void movePlayerBy(int dx, int dy);
    }
}

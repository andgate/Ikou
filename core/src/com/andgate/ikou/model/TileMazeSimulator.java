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
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TileMazeSimulator
{
    private static final String TAG = "TileMaze";

    private Floor floor;
    private Vector3i playerPosition = new Vector3i();
    private ArrayList<WinListener> winListeners = new ArrayList<>();
    private ArrayList<PlayerMoveListener> playerMoveListeners = new ArrayList<>();

    public TileMazeSimulator(Floor floor)
    {
        this.floor = floor;
        this.playerPosition = new Vector3i(floor.getStart());
    }

    public void setFloor(Floor floor)
    {
        this.floor = floor;
        this.playerPosition.set(floor.getStart());
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

    public Vector3i getPlayerPosition()
    {
        return playerPosition;
    }

    Vector3i accumMoveDelta = new Vector3i();
    Vector3i prevAccumMoveDelta = new Vector3i();
    public void move(Vector3i velocity)
    {
        accumMoveDelta.set(0, 0, 0);

        do
        {
            prevAccumMoveDelta.set(accumMoveDelta);
            accumMoveDelta.add(step(velocity));
        }
        while(!accumMoveDelta.equals(prevAccumMoveDelta)
                && !isStopped);

        isStopped = false;

        notifyPlayerMoveListeners(accumMoveDelta.x, accumMoveDelta.z);
    }

    Vector3i moveDelta = new Vector3i();
    private boolean isStopped = false;
    private Vector3i step(Vector3i velocity)
    {
        int x = playerPosition.x + velocity.x;
        int z = playerPosition.z + velocity.z;

        moveDelta.set(0, 0, 0);

        TileSector masterSector = floor.getMasterSector();

        // Is the next position within the map bounds?
        if( (0 <= z && z < masterSector.size)
         && (0 <= x && x < masterSector.get(z).size))
        {
            // Use the next tile to decide
            // what to do.
            TileStack nextTileStack = masterSector.getTileStack(x, z);
            for(int y = 0; y < nextTileStack.size; y++)
            {
                if (nextTileStack.size == 2)
                {
                    y = nextTileStack.size - 1;
                }

                TileData nextTile = nextTileStack.get(y);
                switch (nextTile.getType()) {
                    case Smooth:
                        moveDelta.set(velocity);
                        playerPosition.add(moveDelta);
                        break;
                    case Rough:
                        moveDelta.set(velocity);
                        playerPosition.add(moveDelta);
                        isStopped = true;
                        break;
                    case Obstacle:
                        isStopped = true;
                        break;
                    case End:
                        moveDelta.set(velocity);
                        playerPosition.add(moveDelta);
                        notifyWinListeners();
                        break;
                    default:
                        break;
                }
            }
        }

        return moveDelta;
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

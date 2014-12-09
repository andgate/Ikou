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

package com.andgate.ikou.Model;

import com.andgate.ikou.Constants;
import com.andgate.ikou.TileCode;
import com.badlogic.gdx.math.Vector2;

public class TileMaze
{
    public static enum Direction
    {
        Up(0, 1), Down(0, -1), Left(1, 0), Right(-1, 0), None(0, 0);

        private final int xVel;
        private final int yVel;

        private Direction(int xVel, int yVel)
        {
            this.xVel = xVel;
            this.yVel = yVel;
        }

        public int xVel()
        {
            return xVel;
        }

        public int yVel()
        {
            return yVel;
        }
    }

    private char[][] tiles;
    private final Vector2 initialPlayerPosition;
    private Vector2 playerPosition;
    private Vector2 nextPosition;
    private Direction playerVelocity = Direction.None;

    public TileMaze(char[][] tiles, Vector2 initialPlayerPosition)
    {
        this.tiles = tiles;
        this.initialPlayerPosition = new Vector2(initialPlayerPosition);
        playerPosition = new Vector2(initialPlayerPosition);
        nextPosition = new Vector2(playerPosition);
    }

    public char[][] getTiles()
    {
        return tiles;
    }

    public Vector2 getInitialPlayerPosition()
    {
        return initialPlayerPosition;
    }

    public Vector2 getPlayerPosition()
    {
        return playerPosition;
    }

    public Direction getPlayerVelocity()
    {
        return playerVelocity;
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

    public void move(Direction playerVelocity)
    {
        this.playerVelocity = playerVelocity;

        // While the player is still moving...
        while(this.playerVelocity != Direction.None)
        {
            step();
        }
    }

    private void step()
    {
        nextPosition.add(playerVelocity.xVel(), playerVelocity.yVel());

        int x = (int)nextPosition.x;
        int y = (int)nextPosition.y;

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
                    playerPosition.set(nextPosition);
                    break;
                case TileCode.ROUGH_TILE:
                    playerPosition.set(nextPosition);
                    playerVelocity = Direction.None;
                    break;
                case TileCode.OBSTACLE_TILE:
                    playerVelocity = Direction.None;
                    break;
                default:
                    playerVelocity = Direction.None;
                    break;
            }
        }
        else
        {
            playerVelocity = Direction.None;
        }

        if(!nextPosition.epsilonEquals(playerPosition, Constants.LITTLE_EPSILON))
        {
            nextPosition.set(playerPosition);
        }

    }

    @Override
    public String toString()
    {
        String out = "";

        out += "Player position: ";
        out += playerPosition.toString();
        out += '\n';

        out += "Next position: ";
        out += nextPosition.toString();
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
}

package com.andgate.ikou.model.Tiles;

import com.andgate.ikou.TileCode;
import com.badlogic.gdx.utils.Array;

public class TileStack extends Array<TileData>
{
    public TileStack(char tileCode)
    {
        super();

        switch(tileCode)
        {
            case TileCode.SMOOTH_TILE:
                add(new TileData());
                break;
            case TileCode.OBSTACLE_TILE:
                add(new TileData());
                add(new ObstacleTileData());
                break;
            case TileCode.ROUGH_TILE:
                add(new RoughTileData());
                break;
            case TileCode.END_TILE:
                add(new EndTileData());
                break;
            default:
                add(new BlankTileData());
                break;
        }
    }
}

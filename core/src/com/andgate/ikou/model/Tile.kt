package com.andgate.ikou.model

import com.andgate.ikou.constants.*
import com.badlogic.gdx.graphics.Color

class Tile(var type: Tile.Type) {
    enum class Type {
        SMOOTH, STICKY, OBSTACLE, DROP, FINISH
    }


    fun colorOf(): Color {
        when (type) {
            Tile.Type.SMOOTH -> return SMOOTH_TILE_COLOR
            Tile.Type.STICKY -> return STICKY_TILE_COLOR
            Tile.Type.OBSTACLE -> return OBSTACLE_TILE_COLOR
            Tile.Type.DROP -> return DROP_TILE_COLOR
            Tile.Type.FINISH -> return FINISH_TILE_COLOR
        }
    }
}
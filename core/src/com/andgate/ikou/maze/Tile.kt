package com.andgate.ikou.maze

import com.andgate.ikou.constants.*
import com.badlogic.gdx.graphics.Color

class Tile(var type: Type) {
    enum class Type {
        SMOOTH, STICKY, OBSTACLE, DROP, FINISH
    }


    fun colorOf(): Color {
        when (type) {
            Type.SMOOTH -> return SMOOTH_TILE_COLOR
            Type.STICKY -> return STICKY_TILE_COLOR
            Type.OBSTACLE -> return OBSTACLE_TILE_COLOR
            Type.DROP -> return DROP_TILE_COLOR
            Type.FINISH -> return FINISH_TILE_COLOR
        }
    }
}
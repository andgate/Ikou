package com.andgate.ikou.model

class Tile(var type: Tile.Type = Tile.Type.EMPTY) {
    enum class Type {
        SMOOTH, STICKY, OBSTACLE, EMPTY, DROP, FINISH
    }
}
package com.andgate.ikou.model

class Tile @JvmOverloads constructor(var type: Tile.Type = Tile.Type.EMPTY) {
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

    enum class Type {
        SMOOTH, STICKY, OBSTACLE, EMPTY, DROP, FINISH
    }
}
package com.andgate.ikou.constants

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute


// Default measurements
const val TILE_CELL_SPAN = 1.0f
const val WALL_THICKNESS = 0.1f
const val WALL_HALF_THICKNESS = WALL_THICKNESS / 2f

const val TILE_SPAN = TILE_CELL_SPAN - WALL_HALF_THICKNESS * 2f
const val TILE_HALF_SPAN: Float = TILE_SPAN / 2f
const val TILE_HEIGHT = TILE_CELL_SPAN / 25f
const val TILE_HALF_HEIGHT: Float = TILE_CELL_SPAN / 2f

const val WALL_HEIGHT = TILE_HEIGHT * 2f

const val FLOOR_SPACING = 10f
const val SECTOR_SPAN = 16 // in tiles per side


// Default material and colors
val TILE_MATERIAL = Material(BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA))

val STICKY_TILE_COLOR = Color(Color.DARK_GRAY)
val SMOOTH_TILE_COLOR = Color(Color.LIGHT_GRAY)
val OBSTACLE_TILE_COLOR = Color(Color.BLUE)
val DROP_TILE_COLOR = Color(Color.RED)
val FINISH_TILE_COLOR = Color(Color.RED)
val BACKGROUND_COLOR = Color(Color.WHITE)

val PLAYER_TILE_COLOR = Color(Color.ORANGE)
val TILE_WALL_COLOR = OBSTACLE_TILE_COLOR
package com.andgate.ikou;

public class Constants
{
    public static final float TIME_STEP = 1.0f / 60.0f;

    // Suggested iteration count from the box2d documentation
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;

    public static final int WORLD_HEIGHT = 10;

    public static final float EPSILON = 1.0f / 1E14f;
    public static final float BIG_EPSILON = 1.0f / 1E3f;

    public static final float TILE_LENGTH = 1.0f;
}

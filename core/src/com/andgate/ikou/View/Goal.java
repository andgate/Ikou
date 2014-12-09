package com.andgate.ikou.View;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public class Goal extends Tile
{
    public Goal(Vector3 position)
    {
        super(position);
    }

    @Override
    public void render(ModelBatch modelBatch, Environment environment)
    {
        // Don't render anything at this point
        // Maybe some cool looking tile later?
    }
}

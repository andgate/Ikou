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

package com.andgate.ikou.view;

import com.andgate.ikou.model.Level;
import com.andgate.ikou.model.TileMaze;
import com.andgate.ikou.render.PlayerModelRender;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Player implements Disposable, TileMaze.WinListener
{
    private static final String TAG = "Player";

    private final Level level;

    private static final float SPEED = 15.0f;
    private boolean isMoving = false;
    private boolean isFalling = false;
    private final Vector3 destination = new Vector3();

    PlayerModelRender playerModel = new PlayerModelRender();

    public Player(Level level)
    {
        this.level = level;
        setPosition(level.getIntialPlayerPostion());
        destination.set(getPosition());
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(playerModel, environment);
    }

    Vector3 prevPosition = new Vector3();
    Vector3 currPosition = new Vector3();
    private float tweenTime = 0.0f;
    private float accumulator = 0.0f;

    public void update(float delta)
    {
        if(isMoving)
        {
            updateMovement(delta);
        }
    }

    private void updateMovement(float delta)
    {
        float percentTween = delta / tweenTime;
        accumulator += percentTween;

        if(accumulator >= 1.0f)
        {
            isMoving = false;

            setPosition(destination);
            accumulator = 0.0f;
        }
        else
        {
            currPosition.set(prevPosition);
            currPosition.lerp(destination, accumulator);
            setPosition(currPosition);
        }
    }

    private void updateFall(float delta)
    {

    }

    private Vector3 distance = new Vector3();

    public void moveTo(Vector3 destination)
    {
        isMoving = true;

        distance.set(destination);
        distance.sub(getPosition());
        tweenTime = distance.len() / SPEED;
        prevPosition.set(getPosition());
        this.destination.set(destination);
    }

    public PlayerModelRender getModel()
    {
        return playerModel;
    }

    public boolean isMoving()
    {
        return isMoving;
    }
    public boolean isFalling()
    {
        return isMoving;
    }

    public void setPosition(Vector3 position)
    {
        playerModel.transform.idt();
        playerModel.transform.translate(position);
    }

    Vector3 position = new Vector3();
    public Vector3 getPosition()
    {
        playerModel.transform.getTranslation(position);
        return position;
    }

    @Override
    public void dispose()
    {
        playerModel.dispose();
    }

    @Override
    public void mazeWon()
    {
        isFalling = true;
    }
}

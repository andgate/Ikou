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

package com.andgate.ikou.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class Player extends Tile
{
    private static final float SPEED = 15.0f;
    private boolean isMoving = false;
    private final Vector3 destination = new Vector3();
    private final Vector3 velocity = new Vector3();

    public Player(Vector3 position)
    {
        super(position);
        destination.set(position);

        Material playerMaterial = new Material(ColorAttribute.createDiffuse(Color.CYAN));
        tileModelInstance.materials.get(0).set(playerMaterial);
    }

    Vector3 prevPosition = new Vector3();
    Vector3 currPosition = new Vector3();
    private float tweenTime = 0.0f;
    private float accumulator = 0.0f;

    public void update(float delta)
    {
        if(isMoving)
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

    public boolean isMoving()
    {
        return isMoving;
    }
}

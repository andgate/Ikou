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
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class Obstacle extends Tile
{
    Tile obstacleTile;
    public Obstacle(Vector3 position)
    {
        super(position);
        obstacleTile = new Tile(position);
        obstacleTile.translate(new Vector3(0.0f, Tile.HEIGHT, 0.0f));

        Material playerMaterial = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        obstacleTile.tileModelInstance.materials.get(0).set(playerMaterial);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        super.render(modelBatch, environment);
        obstacleTile.render(modelBatch, environment);
    }
}

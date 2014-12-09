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

import com.andgate.ikou.Constants;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class Tile implements Disposable
{
    public static final float WIDTH = Constants.TILE_LENGTH;
    public static final float HEIGHT = Constants.TILE_THICKNESS;
    public static final float DEPTH = Constants.TILE_LENGTH;

    private final Vector3 position = new Vector3();

    private static TileFactory tileFactory;
    protected ModelInstance tileModelInstance;

    public Tile(Vector3 position)
    {
        if(tileFactory == null)
        {
            tileFactory = new TileFactory();
        }

        this.position.set(position);

        tileModelInstance = tileFactory.construct();
        tileModelInstance.transform.setTranslation(position);
    }

    public void translate(Vector3 translation)
    {
        setPosition(getPosition().add(translation));
    }

    public void setPosition(Vector3 position)
    {
        this.position.set(position);
        tileModelInstance.transform.idt();
        tileModelInstance.transform.setTranslation(position);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(tileModelInstance, environment);
    }

    @Override
    public void dispose()
    {
    }

    private static class TileFactory implements Disposable
    {
        public final Model tileModel;

        public TileFactory()
        {
            ModelBuilder modelBuilder = new ModelBuilder();
            int tileAttributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
            Material tileMaterial = new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY));
            tileModel = modelBuilder.createBox(Tile.WIDTH, Tile.HEIGHT, Tile.DEPTH, tileMaterial, tileAttributes);
        }

        public ModelInstance construct() {
            return new ModelInstance(tileModel);
        }

        @Override
        public void dispose () {
            tileModel.dispose();
        }
    }
}

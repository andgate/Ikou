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

package com.andgate.ikou.model;

import com.andgate.ikou.render.FloorRender;
import com.andgate.ikou.utility.Vector2i;
import com.andgate.ikou.utility.Vector3i;
import com.andgate.ikou.model.TileStack.Tile;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public class Floor
{
    private MasterSector masterSector;
    private Vector3i start = new Vector3i();
    private Vector3i end = new Vector3i();
    private Vector2i offset = new Vector2i();
    private TilePalette palette = new TilePalette();

    FloorRender floorRender;

    public Floor(MasterSector masterSector, TilePalette palette, Vector3i start, Vector3i end)
    {
        this.masterSector = masterSector;
        this.palette = palette;
        this.start.set(start);
        this.end.set(end);

        floorRender = new FloorRender(this);
    }

    public MasterSector getMasterSector()
    {
        return masterSector;
    }

    public Vector3i getStart()
    {
        return start;
    }

    public Vector3i getEnd()
    {
        return end;
    }

    public TileStack getTileStack(int x, int z)
    {
        return masterSector.getTileStack(z - offset.y, x - offset.x);
    }

    public Tile getTile(int x, int y, int z)
    {
        return masterSector.get(x - offset.x, y, z - offset.y);
    }

    public Vector3 getPosition(Vector3 position)
    {
        return floorRender.transform.getTranslation(position);
    }

    public void setOffset(int x, int z)
    {
        offset.set(x, z);
    }

    public void setPosition(float x, float y, float z)
    {
        floorRender.transform.setTranslation(x, y, z);
    }

    public void render(ModelBatch modelBatch, Environment environment)
    {
        modelBatch.render(floorRender, environment);
    }

    public TilePalette getPalette()
    {
        return palette;
    }

    public FloorRender getRender() { return floorRender; }

    public void shrink()
    {
        masterSector.shrink();
    }

    public void dispose()
    {
        if(floorRender != null)
            floorRender.dispose();
    }
}

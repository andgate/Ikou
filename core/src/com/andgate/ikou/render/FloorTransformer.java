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

package com.andgate.ikou.render;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class FloorTransformer
{
    private final Matrix4 transform = new Matrix4();

    private Vector3 position = new Vector3();
    private static final Vector3 rotationAxis = new Vector3(0.0f, 1.0f, 0.0f);
    private float rotationAngle = 0.0f;
    private Vector3 size = new Vector3();
    private float scale = 1.0f;

    public FloorTransformer set(FloorTransformer other)
    {
        transform.set(other.getTransform());
        // Maybe set other properties as well.

        return this;
    }

    public Matrix4 getTransform()
    {
        return transform;
    }

    public void setPosition(Vector3 position)
    {
        this.position.set(position);
    }

    public void setPosition(float x, float y, float z)
    {
        this.position.set(x, y, z);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setSize(float width, float height, float depth)
    {
        size.set(width, height, depth);
    }

    public Vector3 getSize()
    {
        return size;
    }

    public float getWidth()
    {
        return size.x * scale;
    }

    public float getHeight()
    {
        return size.y * scale;
    }

    public float getDepth()
    {
        return size.z * scale;
    }

    public FloorTransformer translate(Vector3 translation)
    {
        position.add(translation);
        return this;
    }

    public FloorTransformer translate(float x, float y, float z)
    {
        position.add(x, y, z);
        return this;
    }

    public FloorTransformer rotateDeg(float angleDelta)
    {
        rotationAngle += angleDelta;
        return this;
    }

    public FloorTransformer rotateRad(float angleDelta)
    {
        rotationAngle += angleDelta * 180f / Math.PI;
        return this;
    }

    public FloorTransformer scaleToBoxSize(float length)
    {
        float floorLength = size.x > size.z ? size.x : size.z;
        scale = length / floorLength;

        return this;
    }

    public FloorTransformer centerOnOrigin()
    {
        setPosition(getWidth() / -2.0f, 0.0f, getDepth() / -2.0f);
        return this;
    }

    public void update()
    {
        transform.idt().scl(scale);
        rotateAroundOrigin();

        transform.trn(position);
    }


    Vector3 tmpVec = new Vector3();
    private void rotateAroundOrigin()
    {
        tmpVec.set(0, 0, 0);
        tmpVec.sub(position);
        transform.translate(tmpVec);
        transform.rotate(rotationAxis, rotationAngle);
        //tmpVec.rotate(rotationAxis, rotationAngle);
        transform.translate(-tmpVec.x, -tmpVec.y, -tmpVec.z);
    }

    public void reset()
    {
        transform.idt();

        position.setZero();
        scale = 1.0f;
        rotationAngle = 0.0f;
    }
}

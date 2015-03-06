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

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.utility.FloatStack;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

import java.util.LinkedList;

public class TileMesh implements Disposable
{
    private static final String TAG = "TileMeshBuilder";

    // Position attribute - (x, y, z)
    public static final String POSITION_ATTRIBUTE = "a_position";
    public static final int POSITION_COMPONENTS = 3;
    // Packed Color attribute - abgr
    public static final String COLOR_ATTRIBUTE = "a_color";
    public static final int COLOR_COMPONENTS = 1;
    // Opengl still expects 4 color components
    public static final int COLOR_COMPONENTS_EXPECTED = 4;
    // Normal Attribute - (
    public static final String NORMAL_ATTRIBUTE = "a_normal";
    public static final int NORMAL_COMPONENTS = 3;
    // Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS + NORMAL_COMPONENTS;

    public static final int VERTICES_PER_FACE = 4;
    public static final int INDICES_PER_FACE = 6;

    public static final int SUBQUADS = 10;


    // Normal pointers for all the six sides. They never changes.
    protected static final Vector3 frontNormal = new Vector3(0.0f, 0.0f, 1.0f);
    protected static final Vector3 backNormal = new Vector3(0.0f, 0.0f, -1.0f);
    protected static final Vector3 rightNormal = new Vector3(1.0f, 0.0f, 0.0f);
    protected static final Vector3 leftNormal = new Vector3(-1.0f, 0.0f, 0.0f);
    protected static final Vector3 topNormal = new Vector3(0.0f, 1.0f, 0.0f);
    protected static final Vector3 bottomNormal = new Vector3(0.0f, -1.0f, 0.0f);

    protected final Vector3[] points = new Vector3[8];
    // To keep memory (and CPU) usage down we will reuse the same Vector3 instances.
    private static final Vector3 pointVector0 = new Vector3();
    private static final Vector3 pointVector1 = new Vector3();
    private static final Vector3 pointVector2 = new Vector3();
    private static final Vector3 pointVector3 = new Vector3();
    private static final Vector3 pointVector4 = new Vector3();
    private static final Vector3 pointVector5 = new Vector3();
    private static final Vector3 pointVector6 = new Vector3();
    private static final Vector3 pointVector7 = new Vector3();

    private FloatArray vertices;
    private ShortArray indicies;
    private Mesh mesh;
    private final Matrix4 transform = new Matrix4();

    public TileMesh()
    {
        vertices = new FloatArray();
        indicies = new ShortArray();
    }

    public Matrix4 getTransform()
    {
        return transform;
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public void addTile(Color color, float x, float y, float z)
    {
        calculateVerts(x, y, z);
        addFront(color);
        addBack(color);
        addRight(color);
        addLeft(color);
        addTop(color);
        addBottom(color);
    }

    public void calculateVerts(float x, float y, float z)
    {
        final float width = TileStack.WIDTH;
        final float height = TileStack.HEIGHT;
        final float depth = TileStack.DEPTH;

        calculateVerts(x, y, z, width, height, depth);
    }

    public void calculateVerts(float x, float y, float z, float width, float height, float depth)
    {
        // Creates the 8 vector points that exists on a box. Those will be used to create the vertex.
        points[0] = pointVector0.set(x,         y,          z + depth);
        points[1] = pointVector1.set(x + width, y,          z + depth);
        points[2] = pointVector2.set(x + width, y + height, z + depth);
        points[3] = pointVector3.set(x,         y + height, z + depth);
        points[4] = pointVector4.set(x + width, y,          z);
        points[5] = pointVector5.set(x,         y,          z);
        points[6] = pointVector6.set(x,         y + height, z);
        points[7] = pointVector7.set(x + width, y + height, z);
    }

    public void addFace(Color color, Vector3 point0, Vector3 point1, Vector3 point2, Vector3 point3, Vector3 normal)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;
        float colorBits = color.toFloatBits();

        vertices.addAll(
                point0.x, point0.y, point0.z, colorBits, normal.x, normal.y, normal.z,
                point1.x, point1.y, point1.z, colorBits, normal.x, normal.y, normal.z,
                point2.x, point2.y, point2.z, colorBits, normal.x, normal.y, normal.z,
                point3.x, point3.y, point3.z, colorBits, normal.x, normal.y, normal.z);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    // Code to subdivide the top face.
    // Causes the map to have trouble rendering, cause too many polys,
    // when SUBQUADS is around 5.
    // Disabled for now, may renable for smooth lighting.
    // Or a shader may be used instead!
    /*void addTop(TileData tile, Color color, float x, float y, float z)
    {
        for (int hDiv = 0; hDiv < SUBQUADS; hDiv++)
        {
            for (int vDiv = 0; vDiv < SUBQUADS; vDiv++)
            {
                calculateVerts(
                        x + hDiv * WIDTH / SUBQUADS,
                        y,
                        z + vDiv * DEPTH / SUBQUADS,
                        WIDTH / SUBQUADS,
                        HEIGHT,
                        DEPTH / SUBQUADS
                );

                int vertexOffset = vertices.size / NUM_COMPONENTS;

                vertices.addAll(
                        points[3].x, points[3].y, points[3].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                        points[2].x, points[2].y, points[2].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                        points[7].x, points[7].y, points[7].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                        points[6].x, points[6].y, points[6].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2]);

                indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
            }
        }

        calculateVerts(x, y, z, WIDTH, HEIGHT, DEPTH);
    }*/

    public void addFront(Color color)
    {
        addFace(color, points[0], points[1], points[2], points[3], frontNormal);
    }

    public void addBack(Color color)
    {
        addFace(color, points[4], points[5], points[6], points[7], backNormal);
    }

    public void addRight(Color color)
    {
        addFace(color, points[1], points[4], points[7], points[2], rightNormal);
    }

    public void addLeft(Color color)
    {
        addFace(color, points[5], points[0], points[3], points[6], leftNormal);
    }

    public void addTop(Color color)
    {
        addFace(color, points[3], points[2], points[7], points[6], topNormal);
    }

    void addBottom(Color color)
    {
        addFace(color, points[5], points[4], points[1], points[0], topNormal);
    }

    public void build()
    {
        float[] v = vertices.toArray();
        short[] i = indicies.toArray();

        mesh = new Mesh(
                true, VERTICES_PER_FACE * (vertices.size / NUM_COMPONENTS), INDICES_PER_FACE * indicies.size,
                new VertexAttribute(Usage.Position, POSITION_COMPONENTS, POSITION_ATTRIBUTE),
                new VertexAttribute(Usage.ColorPacked, COLOR_COMPONENTS_EXPECTED, COLOR_ATTRIBUTE),
                new VertexAttribute(Usage.Normal, NORMAL_COMPONENTS, NORMAL_ATTRIBUTE));
        mesh.setVertices(v);
        mesh.setIndices(i);

        // Clear everything so it can be garbage collected
        //vertices = null;
        //indicies = null;
    }

    @Override
    public void dispose()
    {
        if(mesh != null)
            mesh.dispose();
    }
}

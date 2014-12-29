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
import com.andgate.ikou.model.tile.TileData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class TileMeshBuilder
{
    private static final float WIDTH = Constants.TILE_LENGTH;
    private static final float HEIGHT = Constants.TILE_THICKNESS;
    private static final float DEPTH = Constants.TILE_LENGTH;

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
    private static final float[] frontNormal = new float[]{0.0f, 0.0f, 1.0f};
    private static final float[] backNormal = new float[]{0.0f, 0.0f, -1.0f};
    private static final float[] rightNormal = new float[]{1.0f, 0.0f, 0.0f};
    private static final float[] leftNormal = new float[]{-1.0f, 0.0f, 0.0f};
    private static final float[] topNormal = new float[]{0.0f, 1.0f, 0.0f};
    private static final float[] bottomNormal = new float[]{0.0f, -1.0f, 0.0f};

    final Vector3[] points = new Vector3[8];
    // To keep memory (and CPU) usage down we will reuse the same Vector3 instances.
    private static final Vector3 pointVector0 = new Vector3();
    private static final Vector3 pointVector1 = new Vector3();
    private static final Vector3 pointVector2 = new Vector3();
    private static final Vector3 pointVector3 = new Vector3();
    private static final Vector3 pointVector4 = new Vector3();
    private static final Vector3 pointVector5 = new Vector3();
    private static final Vector3 pointVector6 = new Vector3();
    private static final Vector3 pointVector7 = new Vector3();

    private float[] v;
    private short[] i;
    private FloatArray vertices;
    private ShortArray indicies;
    private Mesh mesh;

    public TileMeshBuilder()
    {
        vertices = new FloatArray();
        indicies = new ShortArray();
    }

    public void addTile(TileData tile, Color color, float x, float y, float z)
    {
        calculateVerts(x, y, z);
        addFront(tile, color);
        addBack(tile, color);
        addRight(tile, color);
        addLeft(tile, color);
        addTop(tile, color);
        addBottom(tile, color);
    }

    public void calculateVerts(float x, float y, float z)
    {
        final float width = TileData.WIDTH;
        final float height = TileData.HEIGHT;
        final float depth = TileData.DEPTH;
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

    public void addFront(TileData tile, Color color)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;

        vertices.addAll(
                points[0].x, points[0].y, points[0].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2],
                points[1].x, points[1].y, points[1].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2],
                points[2].x, points[2].y, points[2].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2],
                points[3].x, points[3].y, points[3].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    public void addBack(TileData tile, Color color)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;

        vertices.addAll(
                points[4].x, points[4].y, points[4].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2],
                points[5].x, points[5].y, points[5].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2],
                points[6].x, points[6].y, points[6].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2],
                points[7].x, points[7].y, points[7].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    public void addRight(TileData tile, Color color)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;

        vertices.addAll(
                points[1].x, points[1].y, points[1].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2],
                points[4].x, points[4].y, points[4].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2],
                points[7].x, points[7].y, points[7].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2],
                points[2].x, points[2].y, points[2].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2]);
        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    public void addLeft(TileData tile, Color color)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;

        vertices.addAll(
                points[5].x, points[5].y, points[5].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2],
                points[0].x, points[0].y, points[0].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2],
                points[3].x, points[3].y, points[3].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2],
                points[6].x, points[6].y, points[6].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    public void addTop(TileData tile, Color color)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;

        vertices.addAll(
                points[3].x, points[3].y, points[3].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                points[2].x, points[2].y, points[2].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                points[7].x, points[7].y, points[7].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                points[6].x, points[6].y, points[6].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    // Code to subdivide the top.
    // Causes the map to have trouble rendering
    // when SUBQUADS is around 5.
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


    void addBottom(TileData tile, Color color)
    {
        int vertexOffset = vertices.size / NUM_COMPONENTS;

        vertices.addAll(
                points[5].x, points[5].y, points[5].z, color.toFloatBits(), bottomNormal[0], bottomNormal[1], bottomNormal[2],
                points[4].x, points[4].y, points[4].z, color.toFloatBits(), bottomNormal[0], bottomNormal[1], bottomNormal[2],
                points[1].x, points[1].y, points[1].z, color.toFloatBits(), bottomNormal[0], bottomNormal[1], bottomNormal[2],
                points[0].x, points[0].y, points[0].z, color.toFloatBits(), bottomNormal[0], bottomNormal[1], bottomNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    public Mesh build()
    {
        v = vertices.toArray();
        i = indicies.toArray();

        mesh = null;
        mesh = new Mesh(true, VERTICES_PER_FACE * (vertices.size / NUM_COMPONENTS), INDICES_PER_FACE * indicies.size,
                new VertexAttribute(Usage.Position, POSITION_COMPONENTS, POSITION_ATTRIBUTE),
                new VertexAttribute(Usage.ColorPacked, COLOR_COMPONENTS_EXPECTED, COLOR_ATTRIBUTE),
                new VertexAttribute(Usage.Normal, NORMAL_COMPONENTS, NORMAL_ATTRIBUTE));
        mesh.setVertices(v);
        mesh.setIndices(i);

        // Clear everything so it can be garbage collected
        vertices.clear();
        indicies.clear();
        vertices = null;
        indicies = null;
        v = null;
        i = null;

        return mesh;
    }
}

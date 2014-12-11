package com.andgate.ikou.Render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Tiles.TileData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
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

    public void addTile(TileData tile, float x, float y, float z)
    {
        setupMesh(x, y, z);
        addFront(tile);
        addBack(tile);
        addRight(tile);
        addLeft(tile);
        addTop(tile);
        addBottom(tile);
    }

    private void setupMesh(float x, float y, float z)
    {
        // Creates the 8 vector points that exists on a box. Those will be used to create the vertex.
        points[0] = pointVector0.set(x,         y,          z + DEPTH);
        points[1] = pointVector1.set(x + WIDTH, y,          z + DEPTH);
        points[2] = pointVector2.set(x + WIDTH, y + HEIGHT, z + DEPTH);
        points[3] = pointVector3.set(x,         y + HEIGHT, z + DEPTH);
        points[4] = pointVector4.set(x + WIDTH, y,          z);
        points[5] = pointVector5.set(x,         y,          z);
        points[6] = pointVector6.set(x,         y + HEIGHT, z);
        points[7] = pointVector7.set(x + WIDTH, y + HEIGHT, z);
    }

    void addFront(TileData tile)
    {
        Color color = tile.getColor();
        int vertexOffset = vertices.size / NUM_COMPONENTS;
        vertices.addAll(
                points[0].x, points[0].y, points[0].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2],
                points[1].x, points[1].y, points[1].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2],
                points[2].x, points[2].y, points[2].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2],
                points[3].x, points[3].y, points[3].z, color.toFloatBits(), frontNormal[0], frontNormal[1], frontNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    void addBack(TileData tile)
    {
        Color color = tile.getColor();
        int vertexOffset = vertices.size / NUM_COMPONENTS;
        vertices.addAll(
                points[4].x, points[4].y, points[4].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2],
                points[5].x, points[5].y, points[5].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2],
                points[6].x, points[6].y, points[6].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2],
                points[7].x, points[7].y, points[7].z, color.toFloatBits(), backNormal[0], backNormal[1], backNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    void addRight(TileData tile)
    {
        Color color = tile.getColor();
        int vertexOffset = vertices.size / NUM_COMPONENTS;
        vertices.addAll(
                points[1].x, points[1].y, points[1].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2],
                points[4].x, points[4].y, points[4].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2],
                points[7].x, points[7].y, points[7].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2],
                points[2].x, points[2].y, points[2].z, color.toFloatBits(), rightNormal[0], rightNormal[1], rightNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    void addLeft(TileData tile)
    {
        Color color = tile.getColor();
        int vertexOffset = vertices.size / NUM_COMPONENTS;
        vertices.addAll(
                points[5].x, points[5].y, points[5].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2],
                points[0].x, points[0].y, points[0].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2],
                points[3].x, points[3].y, points[3].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2],
                points[6].x, points[6].y, points[6].z, color.toFloatBits(), leftNormal[0], leftNormal[1], leftNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));
    }

    void addTop(TileData tile)
    {
        Color color = tile.getColor();
        int vertexOffset = vertices.size / NUM_COMPONENTS;
        vertices.addAll(
                points[3].x, points[3].y, points[3].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                points[2].x, points[2].y, points[2].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                points[7].x, points[7].y, points[7].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2],
                points[6].x, points[6].y, points[6].z, color.toFloatBits(), topNormal[0], topNormal[1], topNormal[2]);

        indicies.addAll((short) (vertexOffset), (short) (1 + vertexOffset), (short) (2 + vertexOffset), (short) (2 + vertexOffset), (short) (3 + vertexOffset), (short) (vertexOffset));

    }

    void addBottom(TileData tile)
    {
        Color color = tile.getColor();
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

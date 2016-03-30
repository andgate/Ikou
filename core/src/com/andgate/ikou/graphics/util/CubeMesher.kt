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

package com.andgate.ikou.graphics.util;

import com.andgate.ikou.constants.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

open class CubeMesher
{
    private val TAG: String = "TileMeshBuilder"

    // Position attribute - (x, y, z)
    val POSITION_ATTRIBUTE: String = "a_position"
    val POSITION_COMPONENTS: Int = 3
    // Packed Color attribute - abgr
    val COLOR_ATTRIBUTE: String = "a_color"
    val COLOR_COMPONENTS: Int = 1
    // Opengl still expects 4 color components
    val COLOR_COMPONENTS_EXPECTED: Int = 4
    // Normal Attribute - (
    val NORMAL_ATTRIBUTE: String = "a_normal";
    val NORMAL_COMPONENTS: Int = 3;
    // Total number of components for all attributes
    val NUM_COMPONENTS: Int = POSITION_COMPONENTS + COLOR_COMPONENTS + NORMAL_COMPONENTS;

    val VERTICES_PER_FACE: Int = 4
    val INDICES_PER_FACE: Int = 6

    // Normal pointers for all the six sides. They never changes.
    protected val frontNormal   = Vector3(0.0f, 0.0f, 1.0f)
    protected val backNormal    = Vector3(0.0f, 0.0f, -1.0f)
    protected val rightNormal   = Vector3(1.0f, 0.0f, 0.0f)
    protected val leftNormal    = Vector3(-1.0f, 0.0f, 0.0f)
    protected val topNormal     = Vector3(0.0f, 1.0f, 0.0f)
    protected val bottomNormal  = Vector3(0.0f, -1.0f, 0.0f)

    protected val points = Array(8, {i -> Vector3()})
    // To keep memory (and CPU) usage down we will reuse the same Vector3 instances.
    private val pointVector0 = Vector3()
    private val pointVector1 = Vector3()
    private val pointVector2 = Vector3()
    private val pointVector3 = Vector3()
    private val pointVector4 = Vector3()
    private val pointVector5 = Vector3()
    private val pointVector6 = Vector3()
    private val pointVector7 = Vector3()

    private val vertices = FloatArray()
    private val indicies = ShortArray()
    private val transform = Matrix4()

    fun calculateVerts(x: Float, y: Float, z: Float)
    {
        val width:  Float = TILE_LENGTH
        val height: Float = TILE_HEIGHT
        val depth:  Float = TILE_LENGTH

        calculateVerts(x, y, z, width, height, depth)
    }

    fun calculateVerts(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float)
    {
        // Creates the 8 vector points that exists on a box. Those will be used to create the vertex.
        points[0] = pointVector0.set(x,         y,          z + depth)
        points[1] = pointVector1.set(x + width, y,          z + depth)
        points[2] = pointVector2.set(x + width, y + height, z + depth)
        points[3] = pointVector3.set(x,         y + height, z + depth)
        points[4] = pointVector4.set(x + width, y,          z)
        points[5] = pointVector5.set(x,         y,          z)
        points[6] = pointVector6.set(x,         y + height, z)
        points[7] = pointVector7.set(x + width, y + height, z)
    }

    fun addAll(color: Color)
    {
        addFront(color)
        addBack(color)
        addLeft(color)
        addRight(color)
        addTop(color)
        addBottom(color)

    }

    fun addFace(color: Color, point0: Vector3, point1: Vector3, point2: Vector3, point3: Vector3, normal: Vector3)
    {
        val vertexOffset: Int = vertices.size / NUM_COMPONENTS
        val colorBits: Float = color.toFloatBits()

        vertices.addAll(
                point0.x, point0.y, point0.z, colorBits, normal.x, normal.y, normal.z,
                point1.x, point1.y, point1.z, colorBits, normal.x, normal.y, normal.z,
                point2.x, point2.y, point2.z, colorBits, normal.x, normal.y, normal.z,
                point3.x, point3.y, point3.z, colorBits, normal.x, normal.y, normal.z)

        indicies.addAll(vertexOffset.toShort(), (1 + vertexOffset).toShort(), (2 + vertexOffset).toShort(), (2 + vertexOffset).toShort(), (3 + vertexOffset).toShort(), vertexOffset.toShort())
    }

    fun addFront(color: Color)
    {
        addFace(color, points[0], points[1], points[2], points[3], frontNormal)
    }

    fun addBack(color: Color)
    {
        addFace(color, points[4], points[5], points[6], points[7], backNormal)
    }

    fun addRight(color: Color)
    {
        addFace(color, points[1], points[4], points[7], points[2], rightNormal)
    }

    fun addLeft(color: Color)
    {
        addFace(color, points[5], points[0], points[3], points[6], leftNormal)
    }

    fun addTop(color: Color)
    {
        addFace(color, points[3], points[2], points[7], points[6], topNormal)
    }

    fun addBottom(color: Color)
    {
        addFace(color, points[5], points[4], points[1], points[0], topNormal)
    }

    fun build(): Mesh
    {
        val v = vertices.toArray();
        val i = indicies.toArray();

        val mesh = Mesh(
                true, VERTICES_PER_FACE * (vertices.size / NUM_COMPONENTS), INDICES_PER_FACE * indicies.size,
                VertexAttribute(Usage.Position, POSITION_COMPONENTS, POSITION_ATTRIBUTE),
                VertexAttribute(Usage.ColorPacked, COLOR_COMPONENTS_EXPECTED, COLOR_ATTRIBUTE),
                VertexAttribute(Usage.Normal, NORMAL_COMPONENTS, NORMAL_ATTRIBUTE))
        mesh.setVertices(v)
        mesh.setIndices(i)

        return mesh
    }
}

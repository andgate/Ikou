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

import com.badlogic.gdx.graphics.Color;

public class TilePalette {
    public Color obstacle = Color.GRAY;
    public Color smooth = Color.LIGHT_GRAY;
    public Color rough = Color.DARK_GRAY;
    public Color player = Color.CYAN;
    public Color end = Color.RED;
    public Color blank = Color.CLEAR;
    public Color background = Color.WHITE;

    public TilePalette()
    {

    }
}

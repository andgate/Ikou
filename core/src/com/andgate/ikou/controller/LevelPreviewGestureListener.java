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

package com.andgate.ikou.controller;

import com.andgate.ikou.view.LevelPreview;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class LevelPreviewGestureListener extends ActorGestureListener
{
    private static final String TAG = "LevelPreviewGestureListener";

    final LevelPreview preview;

    public LevelPreviewGestureListener(LevelPreview preview)
    {
        super(0.05f, 0.4f, 1.1f, 0.15f);
        this.preview = preview;
    }


    private static final float ROTATE_ANGLE = 180f;

    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY)
    {
        if(Math.abs(deltaX) > Math.abs(deltaY)) {
            panHorizontal(deltaX);
        }
        else
        {
            panVertical(deltaY);
        }

    }

    public void panHorizontal(float deltaX)
    {
        float deltaAngleX = ROTATE_ANGLE * deltaX / getTouchDownTarget().getWidth();
        preview.rotateCurrentFloor(deltaAngleX);
    }

    public void panVertical(float deltaY)
    {
        preview.scroll(deltaY);
    }
}

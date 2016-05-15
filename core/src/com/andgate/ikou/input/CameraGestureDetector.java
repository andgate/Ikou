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

package com.andgate.ikou.input;

import com.andgate.ikou.actor.camera.CameraActor;
import com.andgate.ikou.constants.CameraConstantsKt;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class CameraGestureDetector extends GestureDetector
{
    private static final String TAG = "CameraGestureDetector";
    private CameraGestureListener gestureListener;
    private CameraActor camActor;

    float startX, startY;

    public CameraGestureDetector(final CameraActor camActor)
    {
        this(new CameraGestureListener(camActor));
        this.camActor = camActor;
        startX = Gdx.input.getX();
        startY = Gdx.input.getY();
    }

    public CameraGestureDetector(final CameraGestureListener gestureListener)
    {
        super(gestureListener);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT)
        {
            startX = screenX;
            startY = screenY;
        }
        return touchDown((float)screenX, (float)screenY, pointer, button);
    }

    @Override
    public boolean scrolled (int amount)
    {
        float zoomAmount = -amount / CameraConstantsKt.getMAX_PLAYER_DISTANCE();
        camActor.zoom(zoomAmount);
        return false;
    }

    public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        startX = screenX;
        startY = screenY;

        return touchDragged((float)screenX, (float)screenY, pointer);
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY)
    {
        if(Gdx.app.getType() != Application.ApplicationType.Desktop) return false;

        final float deltaX = screenX - startX;
        final float deltaY = screenY - startY;
        startX = screenX;
        startY = screenY;

        camActor.rotateFromDrag(deltaX, deltaY);
        return false;
    }


    private static class CameraGestureListener extends GestureAdapter
    {
        private static final String TAG = "CameraGestureListener";

        private CameraActor camActor;
        private float previousDeltaZoom;
        private float previousDeltaX;
        private float previousDeltaY;

        public CameraGestureListener(CameraActor camActor)
        {
            this.camActor = camActor;
        }

        @Override
        public boolean touchDown (float x, float y, int pointer, int button) {
            previousDeltaZoom = 0;
            previousDeltaX = 0;
            previousDeltaY = 0;
            return false;
        }

        @Override
        public boolean tap (float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress (float x, float y) {
            return false;
        }

        @Override
        public boolean fling (float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan (float x, float y, float deltaX, float deltaY)
        {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance)
        {
            return false;
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
        {
            float initialDistance = initialPointer1.dst(initialPointer2);
            float distance = pointer1.dst(pointer2);
            float newZoom = distance - initialDistance;

            zoom(newZoom);

            float midX1 = (initialPointer1.x + initialPointer2.x) / 2.0f;
            float midY1 = (initialPointer1.y + initialPointer2.y) / 2.0f;

            float midX2 = (pointer1.x + pointer2.x) / 2.0f;
            float midY2 = (pointer1.y + pointer2.y) / 2.0f;

            float newDeltaX = midX2 - midX1;
            float newDeltaY = midY2 - midY1;

            rotate(newDeltaX, newDeltaY);

            return false;
        }

        private void zoom(float newDeltaZoom)
        {
            float amount = newDeltaZoom - previousDeltaZoom;
            previousDeltaZoom = newDeltaZoom;

            float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            float percentZoom = amount / ((w > h) ? w : h);

            camActor.zoom(percentZoom);
        }

        private void rotate(float newDeltaX, float newDeltaY)
        {
            float deltaX = newDeltaX - previousDeltaX;
            float deltaY = newDeltaY - previousDeltaY;

            previousDeltaX = newDeltaX;
            previousDeltaY = newDeltaY;

            camActor.rotateFromDrag(deltaX, deltaY);
        }
    };

}

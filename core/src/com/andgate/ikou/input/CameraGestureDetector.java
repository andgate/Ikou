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

import com.andgate.ikou.Constants;
import com.andgate.ikou.render.ThirdPersonCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraGestureDetector extends GestureDetector
{
    private static final String TAG = "CameraGestureDetector";

    private ThirdPersonCamera camera;

    private float startX, startY;

    private int button = -1;
    private int touched;
    private boolean multiTouch;

    public CameraGestureDetector(final ThirdPersonCamera camera)
    {
        this(new CameraGestureListener(), camera);
    }

    public CameraGestureDetector(final CameraGestureListener gestureListener, final ThirdPersonCamera camera)
    {
        super(gestureListener);
        gestureListener.setController(this);
        this.camera = camera;
    }



    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        touched |= (1 << pointer);
        multiTouch = !MathUtils.isPowerOfTwo(touched);
        if (multiTouch)
            this.button = -1;
        else if (this.button < 0) {
            startX = screenX;
            startY = screenY;
            this.button = button;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        touched &= -1 ^ (1 << pointer);
        multiTouch = !MathUtils.isPowerOfTwo(touched);
        if (button == this.button) this.button = -1;
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        boolean result = super.touchDragged(screenX, screenY, pointer);
        if (result || this.button < 0) return result;
        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        return process(deltaX, deltaY, button);
    }

    protected boolean process (float deltaX, float deltaY, int button)
    {
        camera.rotate(deltaX, deltaY);
        return true;
    }

    protected boolean pinchZoom (float amount)
    {
        camera.zoom(amount);
        return true;
    }

    protected static class CameraGestureListener extends GestureAdapter
    {
        private static final String TAG = "CameraGestureListener";

        private CameraGestureDetector cameraGestureDetector = null;
        private float previousZoom;

        public void setController(CameraGestureDetector cameraGestureDetector)
        {
            this.cameraGestureDetector = cameraGestureDetector;
        }

        public CameraGestureDetector getController()
        {
            return cameraGestureDetector;
        }

        @Override
        public boolean touchDown (float x, float y, int pointer, int button) {
            previousZoom = 0;
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
        public boolean pan (float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean zoom (float initialDistance, float distance)
        {
            float newZoom = distance - initialDistance;
            float amount = newZoom - previousZoom;
            previousZoom = newZoom;

            float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            float percentZoom = amount / ((w > h) ? w : h);

            return cameraGestureDetector.pinchZoom(percentZoom);
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
        {
            return false;
        }
    };

}

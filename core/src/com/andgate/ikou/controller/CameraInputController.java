package com.andgate.ikou.controller;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.view.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraInputController extends GestureDetector implements Player.PlayerPositionListener
{
    private static final String TAG = "CameraInputController";

    private final PerspectiveCamera camera;
    private final Player player;

    private Vector3 target = new Vector3();

    public boolean translateTarget = true;
    public boolean forwardTarget = true;
    public float rotateAngle = 360f;

    protected int button = -1;

    public static final float PINCH_ZOOM_MAX = 1.0f;
    public static final float PINCH_ZOOM_MIN = 0.3f;

    public static final float ANGLE_Y_MIN = Constants.CAMERA_ANGLE_TO_PLAYER - 90.0f;
    public static final float ANGLE_Y_MAX = Constants.CAMERA_ANGLE_TO_PLAYER;

    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    public CameraInputController(final CameraGestureListener gestureListener, final PerspectiveCamera camera, final Player player)
    {
        super(gestureListener);
        gestureListener.setController(this);
        this.camera = camera;
        this.player = player;
        player.addPlayerPositionListener(this);

        setTarget();
    }

    private void setTarget()
    {
        target.set(player.getPosition());
        target.x += TileData.HALF_WIDTH;
        target.z += TileData.HALF_DEPTH;
    }

    public CameraInputController(final PerspectiveCamera camera, final Player player)
    {
        this(new CameraGestureListener(), camera, player);
    }

    public void update (final float delta)
    {
        camera.update();
    }

    private int touched;
    private boolean multiTouch;

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

    private float angleX = 0.0f;
    private float angleY = 0.0f;
    protected boolean process (float deltaX, float deltaY, int button)
    {
        tmpV1.set(camera.direction).crs(camera.up).y = 0f;

        float deltaAngleX = deltaX * -rotateAngle;
        angleX += deltaAngleX;

        float deltaAngleY = deltaY * rotateAngle;
        float tmpAngleY = angleY + deltaAngleY;
        if(inRange(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX))
        {
            angleY = tmpAngleY;
        }
        else
        {
            float bound = pickClosestBound(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX);
            angleY = bound;
            deltaAngleY = bound - angleY;
        }

        camera.rotateAround(target, tmpV1.nor(), deltaAngleY);
        camera.rotateAround(target, Vector3.Y, deltaAngleX);
        camera.update();
        return true;
    }

    private static float pickClosestBound(float n, float low, float high)
    {
        float highDistance = Math.abs(high - n);
        float lowDistance = Math.abs(n - low);

        if(lowDistance < highDistance)
        {
            return low;
        }

        return high;
    }

    private static boolean inRange(float n, float low, float high)
    {
        return (low <= n && n <= high);
    }

    public boolean zoom (float amount)
    {
        camera.fieldOfView = amount * Constants.DEFAULT_FIELD_OF_VIEW;
        camera.update();
        return true;
    }

    protected boolean pinchZoom (float amount) {
        return zoom(amount);
    }


    @Override
    public void playerPositionModified(float dx, float dy, float dz)
    {
        camera.translate(dx, dy, dz);
        target.add(dx, dy, dz);
    }

    protected static class CameraGestureListener extends GestureAdapter
    {
        private static final String TAG = "CameraGestureListener";

        private CameraInputController controller = null;
        private float previousZoom;
        private float totalPercentZoom = 100.f;

        public void setController(CameraInputController controller)
        {
            this.controller = controller;
        }

        public CameraInputController getController()
        {
            return controller;
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
            previousZoom = 0.0f;

            float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            float percentZoom = amount / ((w > h) ? w : h);
            if(totalPercentZoom + percentZoom  < PINCH_ZOOM_MIN)
            {
                totalPercentZoom = PINCH_ZOOM_MIN;
            }
            else if(totalPercentZoom + percentZoom > PINCH_ZOOM_MAX)
            {
                totalPercentZoom = PINCH_ZOOM_MAX;
            }
            else
            {
                // If the zoom provides a change...
                totalPercentZoom += percentZoom;
                previousZoom = newZoom;
            }

            return controller.pinchZoom(totalPercentZoom);
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
        {
            return false;
        }
    };
}

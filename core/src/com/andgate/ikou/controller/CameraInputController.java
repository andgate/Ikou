package com.andgate.ikou.controller;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.tile.TileData;
import com.andgate.ikou.utility.MathExtra;
import com.andgate.ikou.view.PlayerTransformer;
import com.andgate.ikou.view.PlayerTransformer.PlayerTransformListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraInputController extends GestureDetector implements PlayerTransformListener
{
    private static final String TAG = "CameraInputController";

    private final PerspectiveCamera camera;
    private final PlayerTransformer playerTransformer;

    private Vector3 target = new Vector3();

    public final float ROTATE_ANGLE = 360f;

    protected int button = -1;

    /** World units per screen size **/
    public static final float PINCH_ZOOM_FACTOR = 10.0f;
    public static final float MAX_PLAYER_DISTANCE = Constants.FLOOR_SPACING;
    public static final float MIN_PLAYER_DISTANCE = 3.0f;

    public static final float ANGLE_Y_MIN = Constants.CAMERA_ANGLE_TO_PLAYER - 90.0f;
    public static final float ANGLE_Y_MAX = Constants.CAMERA_ANGLE_TO_PLAYER - 5.0f;

    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();

    public CameraInputController(final CameraGestureListener gestureListener, final PerspectiveCamera camera, final PlayerTransformer playerTransformer)
    {
        super(gestureListener);
        gestureListener.setController(this);
        this.camera = camera;
        this.playerTransformer = playerTransformer;
        playerTransformer.addPlayerTransformListener(this);

        setTarget();
    }

    private void setTarget()
    {
        target.set(playerTransformer.getPosition());
        target.x += TileData.HALF_WIDTH;
        target.z += TileData.HALF_DEPTH;
    }

    public CameraInputController(final PerspectiveCamera camera, final PlayerTransformer playerTransformer)
    {
        this(new CameraGestureListener(), camera, playerTransformer);
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

        float deltaAngleX = deltaX * -ROTATE_ANGLE;
        angleX += deltaAngleX;

        float deltaAngleY = deltaY * ROTATE_ANGLE;
        float tmpAngleY = angleY + deltaAngleY;
        if(MathExtra.inRangeInclusive(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX))
        {
            angleY = tmpAngleY;
        }
        else
        {
            float bound = MathExtra.pickClosestBound(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX);
            angleY = bound;
            deltaAngleY = bound - angleY;
        }

        camera.rotateAround(target, tmpV1.nor(), deltaAngleY);
        camera.rotateAround(target, Vector3.Y, deltaAngleX);
        camera.update();
        return true;
    }

    public float getAngleX()
    {
        return angleX;
    }

    public boolean zoom (float amount)
    {
        camera.translate(tmpV1.set(camera.direction).scl(amount));
        camera.update();
        return true;
    }

    protected boolean pinchZoom (float amount)
    {
        float currentDistance = camera.position.dst(playerTransformer.getPosition());

        float displacement = amount * PINCH_ZOOM_FACTOR;
        float newDistance = currentDistance - displacement;

        if(!MathExtra.inRangeExclusive(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE))
        {
            float bound = MathExtra.pickClosestBound(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE);
            displacement = currentDistance - bound;
        }

        return zoom(displacement);
    }


    @Override
    public void playerTransformModified(float dx, float dy, float dz)
    {
        camera.translate(dx, dy, dz);
        target.add(dx, dy, dz);
    }

    protected static class CameraGestureListener extends GestureAdapter
    {
        private static final String TAG = "CameraGestureListener";

        private CameraInputController controller = null;
        private float previousZoom;

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
            previousZoom = newZoom;

            float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            float percentZoom = amount / ((w > h) ? w : h);

            return controller.pinchZoom(percentZoom);
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
        {
            return false;
        }
    };

}

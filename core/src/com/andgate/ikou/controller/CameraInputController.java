package com.andgate.ikou.controller;

import com.andgate.ikou.model.TileMaze;
import com.andgate.ikou.view.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraInputController extends GestureDetector implements Player.PlayerPositionListener
{
    private static final String TAG = "CameraInputController";

    private final Camera camera;
    private final Player player;

    private Vector3 target = new Vector3();

    public boolean translateTarget = true;
    public boolean forwardTarget = true;
    public boolean scrollTarget = false;
    public boolean alwaysScroll = true;

    public int activateKey = 0;
    protected boolean activatePressed;

    public static final float PINCH_ZOOM_FACTOR = 10f;

    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    public CameraInputController(final CameraGestureListener gestureListener, final Camera camera, final Player player)
    {
        super(gestureListener);
        gestureListener.setController(this);
        this.camera = camera;
        this.player = player;
        target = player.getPosition();
        player.addPlayerPositionListener(this);
    }

    public CameraInputController(final Camera camera, final Player player)
    {
        this(new CameraGestureListener(), camera, player);
    }

    public void update (final float delta)
    {
        camera.update();
    }

    public boolean zoom (float amount)
    {
        camera.translate(tmpV1.set(camera.direction).scl(amount));
        camera.update();
        return true;
    }

    protected boolean pinchZoom (float amount) {
        return zoom(PINCH_ZOOM_FACTOR * amount);
    }


    @Override
    public void playerPositionModified(float dx, float dy, float dz)
    {
        camera.translate(dx, dy, dz);
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
            return controller.pinchZoom(amount / ((w > h) ? h : w));
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
        {
            return false;
        }
    };
}

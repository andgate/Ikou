package com.andgate.ikou.render;

import com.andgate.ikou.Constants;
import com.andgate.ikou.model.Player;
import com.andgate.ikou.model.Player.PlayerTransformListener;
import com.andgate.ikou.model.TileStack;
import com.andgate.ikou.utility.MathExtra;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class ThirdPersonCamera extends PerspectiveCamera implements PlayerTransformListener
{
    private static final String TAG = "ThirdPersonCamera";

    /** World units per screen size **/
    public static final float PINCH_ZOOM_FACTOR = 10.0f;
    public static final float MAX_PLAYER_DISTANCE = Constants.FLOOR_SPACING;
    public static final float MIN_PLAYER_DISTANCE = 3.0f;

    public final float ROTATE_ANGLE = 360f;
    public static final float ANGLE_Y_MIN = Constants.CAMERA_ANGLE_TO_PLAYER - 90.0f;
    public static final float ANGLE_Y_MAX = Constants.CAMERA_ANGLE_TO_PLAYER - 10.0f;

    private final Player player;
    private Vector3 target = new Vector3();

    private float angleX = 0.0f;
    private float angleY = 0.0f;
    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();

    public ThirdPersonCamera(Player player)
    {
        super(Constants.DEFAULT_FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.player = player;
        player.addPlayerTransformListener(this);

        initialize();
    }

    private void initialize()
    {
        target.set(player.getPosition());
        target.x += TileStack.HALF_WIDTH;
        target.z += TileStack.HALF_DEPTH;

        super.position.set( target.x,
                            target.y + Constants.CAMERA_VERTICAL_DISTANCE,
                            target.z - Constants.CAMERA_HORIZONTAL_DISTANCE);

        super.lookAt(target);
        super.near = 1f;
        super.far = Constants.CAMERA_FAR;
        super.update();
    }

    public void resize(int viewportWidth, int viewportHeight)
    {
        super.viewportWidth = viewportWidth;
        super.viewportHeight = viewportHeight;
        super.update(true);
    }

    public void zoom(float amount)
    {
        float currentDistance = super.position.dst(player.getPosition());

        float displacement = amount * PINCH_ZOOM_FACTOR;
        float newDistance = currentDistance - displacement;

        if(!MathExtra.inRangeExclusive(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE))
        {
            float bound = MathExtra.pickClosestBound(newDistance, MIN_PLAYER_DISTANCE, MAX_PLAYER_DISTANCE);
            displacement = currentDistance - bound;
        }

        super.translate(tmpV1.set(super.direction).scl(displacement));
        super.update();
    }

    public void rotate(float deltaX, float deltaY)
    {
        tmpV1.set(super.direction).crs(super.up).y = 0f;

        float deltaAngleX = deltaX * -ROTATE_ANGLE;
        angleX += deltaAngleX;

        float deltaAngleY = deltaY * ROTATE_ANGLE;
        float tmpAngleY = angleY + deltaAngleY;
        if(!MathExtra.inRangeInclusive(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX))
        {
            tmpAngleY = MathExtra.pickClosestBound(tmpAngleY, ANGLE_Y_MIN, ANGLE_Y_MAX);
            deltaAngleY = tmpAngleY - angleY;
        }
        angleY = tmpAngleY;

        super.rotateAround(target, tmpV1.nor(), deltaAngleY);
        super.rotateAround(target, Vector3.Y, deltaAngleX);
        super.update();
    }

    public float getAngleX()
    {
        return angleX;
    }

    public float getAngleY()
    {
        return angleY;
    }

    @Override
    public void playerTransformModified(float dx, float dy, float dz)
    {
        super.translate(dx, dy, dz);
        target.add(dx, dy, dz);
        super.update();
    }
}

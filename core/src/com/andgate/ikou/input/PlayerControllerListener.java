package com.andgate.ikou.input;

import com.andgate.ikou.input.mappings.OuyaPad;
import com.andgate.ikou.input.mappings.Xbox360Pad;
import com.andgate.ikou.render.ThirdPersonCamera;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.andgate.ikou.input.PlayerInput.DirectionListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector2;

public class PlayerControllerListener extends ControllerAdapter
{
    private final PlayerInput playerInput;

    private int xAxisLeft = -1;
    private int yAxisLeft = -1;

    private float xAxisValue = 0.0f;
    private float yAxisValue = 0.0f;


    public PlayerControllerListener(DirectionListener directionListener, ThirdPersonCamera camera)
    {
        playerInput = new PlayerInput(directionListener, camera);
    }

    public void update(float delta)
    {
        if(!(xAxisValue == 0 && yAxisValue == 0))
        {
            playerInput.move(xAxisValue, yAxisValue);
            xAxisValue = 0.0f;
            yAxisValue = 0.0f;
        }
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        mapToController(controller);

        float validValue = (Math.abs(value) >= 0.9f) ? value : 0.0f;

        if(axisIndex == xAxisLeft)
        {
            xAxisValue = validValue;
        }
        else if(axisIndex == yAxisLeft)
        {
            yAxisValue = validValue;
        }

        return false;
    }

    private void mapToController(Controller controller)
    {
        if(Xbox360Pad.isXbox360Controller(controller))
        {
            xAxisLeft = Xbox360Pad.AXIS_LEFT_X;
            yAxisLeft = Xbox360Pad.AXIS_LEFT_Y;
        }
        else if(OuyaPad.isOuyaController(controller))
        {
            xAxisLeft = OuyaPad.AXIS_LEFT_X;
            yAxisLeft = OuyaPad.AXIS_LEFT_Y;
        }
        else
        {
            xAxisLeft = -1;
            yAxisLeft = -1;
        }
    }
}

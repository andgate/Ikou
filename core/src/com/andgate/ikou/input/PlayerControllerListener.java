package com.andgate.ikou.input;

import com.andgate.ikou.input.mappings.Xbox360Pad;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.andgate.ikou.input.PlayerInput.DirectionListener;
import com.badlogic.gdx.controllers.Controllers;

public class PlayerControllerListener extends ControllerAdapter
{
    private final PlayerInput playerInput;

    private float yAxis = Xbox360Pad.AXIS_LEFT_Y;
    private float xAxis = Xbox360Pad.AXIS_LEFT_X;

    public PlayerControllerListener(DirectionListener directionListener, CameraInput cameraInput)
    {
        playerInput = new PlayerInput(directionListener, cameraInput);
    }

    public int indexOf (Controller controller) {
        return Controllers.getControllers().indexOf(controller, true);
    }

    @Override
    public void connected (Controller controller) {
        System.out.println("connected " + controller.getName());
        int i = 0;
        for (Controller c : Controllers.getControllers()) {
            System.out.println("#" + i++ + ": " + c.getName());
        }
    }

    @Override
    public void disconnected (Controller controller) {
        System.out.println("disconnected " + controller.getName());
        int i = 0;
        for (Controller c : Controllers.getControllers()) {
            System.out.println("#" + i++ + ": " + c.getName());
        }
        if (Controllers.getControllers().size == 0) System.out.println("No controllers attached");
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        if(Math.abs(value) < 0.5f)
            return false;

        if(axisIndex == xAxis)
        {
            if(value > 0.0f)
            {
                playerInput.move(1.0f, 0.0f);
            }
            else
            {
                playerInput.move(-1.0f, 0.0f);
            }
        }
        if(axisIndex == yAxis)
        {
            if(value > 0.0f)
            {
                playerInput.move(0.0f, 1.0f);
            }
            else
            {
                playerInput.move(0.0f, -1.0f);
            }
        }

        return false;
    }

}

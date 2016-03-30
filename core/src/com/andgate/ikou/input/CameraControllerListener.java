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

import com.andgate.ikou.input.mappings.OuyaPad;
import com.andgate.ikou.input.mappings.Xbox360Pad;
import com.andgate.ikou.graphics.camera.ThirdPersonCamera;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

public class CameraControllerListener extends ControllerAdapter
{
    private final ThirdPersonCamera camera;

    private static final float CAMERA_ZOOM_SPEED = 2.0f;
    private static final float CAMERA_ROTATE_SPEED = 360.0f;

    private int leftTriggerAxis = Xbox360Pad.AXIS_LEFT_TRIGGER;
    private int rightTriggerAxis = Xbox360Pad.AXIS_RIGHT_TRIGGER;
    private int xAxisRight = Xbox360Pad.AXIS_RIGHT_X;
    private int yAxisRight = Xbox360Pad.AXIS_RIGHT_Y;

    private float xAxisRightValue = 0.0f;
    private float yAxisRightValue = 0.0f;
    private float leftTriggerValue = 0.0f;
    private float rightTriggerValue = 0.0f;

    public CameraControllerListener(ThirdPersonCamera camera)
    {
        this.camera = camera;
    }

    public void update(float delta)
    {
        float fullZoomDelta = CAMERA_ZOOM_SPEED * delta;
        float zoomDelta = (leftTriggerValue - rightTriggerValue) * fullZoomDelta;
        camera.zoom(zoomDelta);

        float fullAngleDelta = CAMERA_ROTATE_SPEED * delta;
        float deltaAngleX = xAxisRightValue * fullAngleDelta;
        float deltaAngleY = yAxisRightValue * fullAngleDelta;
        camera.rotate(deltaAngleX, deltaAngleY);
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
        mapToController(controller);

        float validValue = (Math.abs(value) >= 0.1f) ? value : 0.0f;

        if(axisIndex == xAxisRight)
        {
            xAxisRightValue = validValue;
        }
        else if(axisIndex == yAxisRight)
        {
            yAxisRightValue = validValue;
        }
        else if(axisIndex == leftTriggerAxis)
        {
            leftTriggerValue = validValue;
        }
        else if(axisIndex == rightTriggerAxis)
        {
            rightTriggerValue = validValue;
        }

        return false;
    }

    private void mapToController(Controller controller)
    {
        if(Xbox360Pad.isXbox360Controller(controller))
        {
            leftTriggerAxis = Xbox360Pad.AXIS_LEFT_TRIGGER;
            rightTriggerAxis = Xbox360Pad.AXIS_RIGHT_TRIGGER;
            xAxisRight = Xbox360Pad.AXIS_RIGHT_X;
            yAxisRight = Xbox360Pad.AXIS_RIGHT_Y;
        }
        else if(OuyaPad.isOuyaController(controller))
        {
            leftTriggerAxis = OuyaPad.AXIS_LEFT_TRIGGER;
            rightTriggerAxis = OuyaPad.AXIS_RIGHT_TRIGGER;
            xAxisRight = OuyaPad.AXIS_RIGHT_X;
            yAxisRight = OuyaPad.AXIS_RIGHT_Y;
        }
        else
        {
            leftTriggerAxis = -1;
            rightTriggerAxis = -1;
            xAxisRight = -1;
            yAxisRight = -1;
        }
    }
}

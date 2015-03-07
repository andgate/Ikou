package com.andgate.ikou.input;

import com.badlogic.gdx.controllers.ControllerAdapter;
import com.andgate.ikou.input.PlayerInput.DirectionListener;

public class PlayerControllerListener extends ControllerAdapter
{
    private final PlayerInput playerInput;

    public PlayerControllerListener(DirectionListener directionListener, CameraInputController cameraController)
    {
        playerInput = new PlayerInput(directionListener, cameraController);
    }



}

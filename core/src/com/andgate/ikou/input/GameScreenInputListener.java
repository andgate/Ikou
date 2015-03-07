package com.andgate.ikou.input;

import com.andgate.ikou.view.GameScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameScreenInputListener extends InputAdapter
{
    private static final String TAG = "GameScreenInputListener";
    private final GameScreen screen;

    public GameScreenInputListener(GameScreen screen)
    {
        this.screen = screen;
    }

    public boolean keyDown (int keycode) {
        if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE)
        {
            screen.endGame();
        }

        return false;
    }
}

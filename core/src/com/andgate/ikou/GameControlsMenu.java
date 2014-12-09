package com.andgate.ikou;

import com.andgate.ikou.Utility.Icon;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameControlsMenu
{
    private final Ikou game;

    public enum Mode
    {
        MOVEMENT,
        CAMERA
    }

    public Mode currentMode = Mode.MOVEMENT;

    private Stage cameraModeStage;
    private Stage movementModeStage;
    private SpriteBatch batch;

    public GameControlsMenu(Ikou game, InputMultiplexer im, InputProcessor moveController, InputProcessor camController)
    {
        this.game = game;
        batch = new SpriteBatch();

        build(im, moveController, camController);

        // Game always starts in movement mode
        im.addProcessor(movementModeStage);
    }

    private void build(InputMultiplexer im, InputProcessor moveController, InputProcessor camController)
    {
        buildCameraMode(im, moveController, camController);
        buildMovementMode(im, moveController, camController);
    }

    private void buildCameraMode(final InputMultiplexer im, final InputProcessor moveController, final InputProcessor camController)
    {
        cameraModeStage = new Stage();

        ImageButton moveButton = Icon.createIconButton(game, Constants.MOVE_ICON_LOCATION, Constants.MOVE_ICON_DOWN_LOCATION, new ClickListener()
                {
                    @Override
                    public void clicked(InputEvent event, float x, float y)
                    {
                        im.removeProcessor(cameraModeStage);
                        im.removeProcessor(camController);


                        im.addProcessor(movementModeStage);
                        im.addProcessor(moveController);
                        currentMode = Mode.MOVEMENT;
                    }
                });

        Table table = new Table();
        table.bottom().left();
        table.add(moveButton).bottom().left();

        table.setFillParent(true);

        cameraModeStage.addActor(table);
    }

    private void buildMovementMode(final InputMultiplexer im, final InputProcessor moveController, final InputProcessor camController)
    {
        movementModeStage = new Stage();

        ImageButton cameraButton = Icon.createIconButton(game, Constants.CAMERA_ICON_LOCATION, Constants.CAMERA_ICON_DOWN_LOCATION, new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                im.removeProcessor(movementModeStage);
                im.removeProcessor(moveController);

                im.addProcessor(cameraModeStage);
                im.addProcessor(camController);
                currentMode = Mode.CAMERA;
            }
        });

        Table table = new Table();
        table.bottom().left();
        table.add(cameraButton).bottom().left();

        table.setFillParent(true);

        movementModeStage.addActor(table);
    }

    public void render()
    {
        batch.begin();
        switch(currentMode)
        {
            case MOVEMENT:
                movementModeStage.draw();
                break;
            case CAMERA:
                cameraModeStage.draw();
                break;
            default:
                break;
        }
        batch.end();
    }
}

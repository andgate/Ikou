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

package com.andgate.ikou.controller;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;
import com.andgate.ikou.utility.Icon;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;

public class GameControlsMenu implements Disposable
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
    private final SpriteBatch batch;

    private final InputMultiplexer im;
    private final InputProcessor moveController;
    private final InputProcessor camController;

    public GameControlsMenu(Ikou game, InputMultiplexer im, InputProcessor moveController, InputProcessor camController)
    {
        this.game = game;
        batch = new SpriteBatch();

        this.im = im;
        this.moveController = moveController;
        this.camController = camController;

        build();
    }

    public void build()
    {
        buildCameraMode();
        buildMovementMode();

        if(currentMode == Mode.CAMERA)
        {
            im.addProcessor(cameraModeStage);
        }
        else
        {
            im.addProcessor(movementModeStage);
        }
    }

    private void buildCameraMode()
    {
        if(cameraModeStage != null)
        {
            cameraModeStage.dispose();
        }

        cameraModeStage = new Stage();

        ImageButton moveButton = Icon.createIconButton(game, Constants.MOVE_ICON_LOCATION, Constants.MOVE_ICON_DOWN_LOCATION, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

    private void buildMovementMode()
    {
        if(movementModeStage != null)
        {
            movementModeStage.dispose();
        }

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

    public void update()
    {
        cameraModeStage.act();
        movementModeStage.act();
    }

    @Override
    public void dispose()
    {
        cameraModeStage.dispose();
        movementModeStage.dispose();
        batch.dispose();
    }

}

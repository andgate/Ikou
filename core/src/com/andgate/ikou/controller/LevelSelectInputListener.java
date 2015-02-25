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

import com.andgate.ikou.model.LevelData;
import com.andgate.ikou.view.LevelSelectScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;

public class LevelSelectInputListener extends InputListener
{
    private static final String TAG = "LevelSelectInputListener";

    private final List levelList;
    private final LevelSelectScreen screen;
    private final LevelData[] levelDatas;

    private int selectedIndex = 0;

    public LevelSelectInputListener(List levelList, LevelSelectScreen screen, LevelData[] levelDatas)
    {
        super();

        this.levelList = levelList;
        this.screen = screen;
        this.levelDatas = levelDatas;

        selectedIndex = levelList.getSelectedIndex();
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
    {
        //game.buttonPressedSound.play();
        //game.setScreen(new FloorSelectScreen(game, levelData));
        //screen.dispose();

        int currentSelectedIndex = levelList.getSelectedIndex();

        if(currentSelectedIndex != selectedIndex)
        {
            selectedIndex = currentSelectedIndex;
            screen.setSelectedLevel(levelDatas[selectedIndex]);
        }

        return true;
    }
}

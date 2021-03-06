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

package com.andgate.ikou.utility;

import com.andgate.ikou.Constants;
import com.andgate.ikou.Ikou;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Icon
{
    public static ImageButton createIconButton(Ikou game, String upFilename, String downFilename, ClickListener listener)
    {
        Texture buttonTexture = new Texture(Gdx.files.internal(upFilename), true);
        buttonTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        Texture buttonDownTexture = new Texture(Gdx.files.internal(downFilename), true);
        buttonDownTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        TextureRegionDrawable buttonDrawable
                = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        TextureRegionDrawable buttonDownDrawable
                = new TextureRegionDrawable(new TextureRegion(buttonDownTexture));


        ImageButton button = new ImageButton(buttonDrawable, buttonDownDrawable);
        button.getImageCell().width(Constants.BUTTON_LENGTH * game.ppu);
        button.getImageCell().height(Constants.BUTTON_LENGTH * game.ppu);

        button.addListener(listener);

        return button;
    }
}

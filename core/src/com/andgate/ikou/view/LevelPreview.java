package com.andgate.ikou.view;

import com.andgate.ikou.Ikou;
import com.andgate.ikou.model.Level;
import com.andgate.ikou.render.LevelRender;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelPreview
{
    private LevelRender levelRender;

    private final ModelBatch modelBatch;
    private final PerspectiveCamera camera;

    private final Rectangle viewRect = new Rectangle();
    private final Environment environment;

    public LevelPreview(PerspectiveCamera camera)
    {
        this.camera = camera;
        levelRender = null;
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    public void setSize(int x, int y, int w, int h)
    {
        viewRect.set(x, y, w, h);

        camera.viewportWidth = w;
        camera.viewportHeight = h;
        camera.update(true);
    }

    public void setLevelRender(LevelRender levelRender)
    {
        this.levelRender = levelRender;
    }

    public void render(float delta)
    {
        renderLevel();
    }

    public void renderLevel()
    {
        if(levelRender != null)
        {
            Gdx.gl.glViewport((int)viewRect.x, (int)viewRect.x, (int)viewRect.width, (int)viewRect.height);

            modelBatch.begin(camera);
            modelBatch.render(levelRender, environment);
            modelBatch.end();

            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}

package com.andgate.ikou.view;

import com.andgate.ikou.Constants;
import com.andgate.ikou.render.LevelRender;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelPreview
{
    private LevelRender levelRender;

    private final PerspectiveCamera camera;
    private final ModelBatch modelBatch;
    private final Environment environment;

    private FrameBuffer frameBuffer;
    private TextureRegionDrawable drawable;

    public LevelPreview()
    {
        camera = new PerspectiveCamera(Constants.DEFAULT_FIELD_OF_VIEW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setupCamera();

        levelRender = null;
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void setupCamera()
    {
        camera.position.set(0.0f, 1.0f, -3.0f);
        camera.lookAt(0.0f, 0.0f, 0.0f);
        camera.near = 1f;
        camera.far = Constants.CAMERA_FAR;
        camera.update();
    }

    public void setSize(int w, int h)
    {
        if(frameBuffer != null)
            frameBuffer.dispose();

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, true);
        TextureRegion textureRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        textureRegion.flip(false, true);
        drawable = new TextureRegionDrawable(textureRegion);

        camera.viewportWidth = w;
        camera.viewportHeight = h;
        camera.update(true);
    }

    public void setLevelRender(LevelRender levelRender)
    {
        this.levelRender = levelRender;
        if(levelRender != null)
        {
            levelRender.setCamera(camera);
            levelRender.scaleFloorsToBoxSize(3.0f);
            levelRender.centerOnOrigin();
            levelRender.spaceFloors(1.0f);
        }
    }

    public void render(float delta)
    {
        renderLevel();
    }

    public void renderLevel()
    {
        if(levelRender != null)
        {
            frameBuffer.begin();

            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(camera);
            modelBatch.render(levelRender, environment);
            modelBatch.end();

            frameBuffer.end();
        }
    }

    public Drawable getDrawable()
    {
        return drawable;
    }
}

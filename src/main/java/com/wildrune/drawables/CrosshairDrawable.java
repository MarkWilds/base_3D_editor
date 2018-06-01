package com.wildrune.drawables;

import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.GLViewport;
import com.wildrune.gui.viewport.camera.ViewportCamera;
import com.wildrune.util.MathHelper;
import org.joml.Vector3f;

import java.awt.Color;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 19/08/17
 */
public class CrosshairDrawable implements GLViewport.Drawable {

    @Override
    public void render(GLViewport viewport, GLRenderer renderer) {
        ViewportCamera camera = viewport.getCamera();
        float crosshairSize = 6;
        float halfWidth = camera.getViewportWidth() / 2;
        float halfHeight = camera.getViewportHeight() / 2;

        renderer.setLineWidth(3);
        renderer.begin(MathHelper.IDENTITY, camera.getScreenProjectionMatrix());

        renderer.drawLine(new Vector3f(halfWidth - crosshairSize - 1, halfHeight, 0),
            new Vector3f(halfWidth + crosshairSize, halfHeight, 0), Color.white, GLRenderer.LINE_TYPE.NORMAL);
        renderer.drawLine(new Vector3f(halfWidth, halfHeight - crosshairSize - 1, 0),
            new Vector3f(halfWidth, halfHeight + crosshairSize, 0), Color.white, GLRenderer.LINE_TYPE.NORMAL);

        renderer.end();
        renderer.setLineWidth(1);
    }
}

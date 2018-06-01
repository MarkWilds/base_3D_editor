package com.wildrune.gui.viewport.camera;

import com.wildrune.util.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 20/08/17
 */
public class PerspCamera extends ViewportCamera {

    private static final float farCameraView = 1024.0f;

    @Override
    public Matrix4f getViewportProjectionMatrix() {
        viewportProjectionMatrix.identity();
        viewportProjectionMatrix.perspectiveLH((float) Math.toRadians(90), viewportWidth / (float) viewportHeight, 0.1f, farCameraView);

        return viewportProjectionMatrix;
    }

    public void lookAt(Vector3f lookat) {
        Vector3f position = new Vector3f();
        worldMatrix.getTranslation(position);
        worldMatrix.identity();
        worldMatrix.lookAtLH(position, lookat, MathHelper.UNIT_Y).invert();
    }
}

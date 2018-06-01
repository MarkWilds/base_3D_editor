package com.wildrune.gui.viewport.camera;

import org.joml.Matrix4f;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 20/08/17
 */
public class OrthoCamera extends ViewportCamera {
    private float zoom;

    public OrthoCamera() {
        this.zoom = 1.0f;
    }

    @Override
    public Matrix4f getViewportProjectionMatrix() {
        viewportProjectionMatrix.identity();
        viewportProjectionMatrix.orthoLH(-viewportWidth / 2 * zoom, viewportWidth / 2 * zoom,
                -viewportHeight / 2 * zoom, viewportHeight / 2 * zoom, near, far, viewportProjectionMatrix);

        return viewportProjectionMatrix;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}

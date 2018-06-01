package com.wildrune.gui.viewport.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 20/08/17
 */
public abstract class ViewportCamera {

    protected Matrix4f viewportProjectionMatrix;
    protected Matrix4f screenProjectionMatrix;
    protected Matrix4f worldMatrix;
    protected Matrix4f viewMatrix;

    protected float near, far;
    protected int viewportWidth, viewportHeight;

    ViewportCamera() {
        screenProjectionMatrix = new Matrix4f();
        viewportProjectionMatrix = new Matrix4f();
        worldMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    /**
     * Move by a given vector
     * @param movement
     */
    public void move(Vector3f movement) {
        worldMatrix.translateLocal(movement);
    }

    /**
     * rotate the camera by axis angle
     * @param angle in degrees
     * @param axis to rotate about
     */
    public void rotate(float angle, Vector3f axis) {
        worldMatrix.rotate((float) Math.toRadians(angle), axis);
    }

    public abstract Matrix4f getViewportProjectionMatrix();

    public Matrix4f getScreenProjectionMatrix() {
        return new Matrix4f().ortho2DLH(0, viewportWidth, 0, viewportHeight);
    }

    public Matrix4f getWorldMatrix() {
        return new Matrix4f(worldMatrix);
    }

    public Matrix4f getViewMatrix() {
        getWorldMatrix().invert(viewMatrix.identity());
        return viewMatrix;
    }

    public void setWindowDimensions(int width, int height)
    {
        viewportWidth = width;
        viewportHeight = height;
    }

    public void setClipDistance(float near, float far) {
        this.near = near;
        this.far = far;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }
}

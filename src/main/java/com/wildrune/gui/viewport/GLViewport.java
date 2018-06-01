package com.wildrune.gui.viewport;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.camera.ViewportCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 13/08/17
 */
public abstract class GLViewport extends GLJPanel implements GLEventListener, MouseInputListener, MouseWheelListener {

    private final int depth = 1 << 14;

    public enum ViewportType {
        PERSPECTIVE,
        ORTHO_TOP,
        ORTHO_SIDE,
        ORTHO_FRONT
    }

    public interface Drawable {

        void render(GLViewport viewport, GLRenderer renderer);
    }

    private ViewportType type;
    private GLRenderer renderer;
    private List<Drawable> drawableList;

    GLViewport(GLRenderer renderer, ViewportType type) {
        addGLEventListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        this.renderer = renderer;
        this.type = type;
        this.drawableList = new ArrayList<>();
    }

    public abstract void reset();

    public abstract ViewportCamera getCamera();

    public void onInit() {
    }

    public void onDispose() {
    }

    public void onRender(GLRenderer renderer) {
    }

    public void onResize(int x, int y, int width, int height) {
        getCamera().setWindowDimensions(width, height);
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        onInit();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        onDispose();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        renderer.setAutoDrawable(glAutoDrawable);
        renderer.clearBackbuffers();
        drawableList.forEach(drawable -> drawable.render(this, renderer));
        onRender(renderer);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        glAutoDrawable.getGL().glViewport(0, 0, width, height);
        onResize(0, 0, width, height);
    }

    public void addDrawable(Drawable drawable) {
        if (!drawableList.contains(drawable)) {
            drawableList.add(drawable);
        }
    }

    public Vector3f screenToWorld(float x, float y) {
        ViewportCamera camera = getCamera();

        // screen space to clip/ndc space
        float ndcX = 2 * x / camera.getViewportWidth() - 1;
        float ndcY = 1 - 2 * y / camera.getViewportHeight();

        Matrix4f projectionMatrix = camera.getViewportProjectionMatrix();

        // clip clip/ndc camera space
        Vector3f viewportVector = new Vector3f(ndcX / projectionMatrix.m00(),
                ndcY / projectionMatrix.m11(),
                0);

        // camera space to world space
        return camera.getWorldMatrix().transformPosition(viewportVector);
    }

    public Vector3f worldToScreen(Vector3f position) {
        ViewportCamera camera = getCamera();
        Vector3f transformedPosition = new Vector3f(position);

        // world to camera space
        camera.getViewMatrix().transformPosition(transformedPosition);

        // camera to clip/ndc space
        camera.getViewportProjectionMatrix().transformPosition(transformedPosition);

        // from clip/ndc to screen space
        float x = (transformedPosition.x + 1) * camera.getViewportWidth() / 2;
        float y = (transformedPosition.y + 1) * camera.getViewportHeight() / 2;

        return new Vector3f(x, y, 0);
    }

    public ViewportType getType() {
        return type;
    }

    public int getCameraDepth() {
        return depth;
    }

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.6
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.6
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.6
     */
    public void mouseMoved(MouseEvent e) {
    }
}

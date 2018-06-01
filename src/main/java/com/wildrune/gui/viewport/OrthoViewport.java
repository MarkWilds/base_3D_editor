package com.wildrune.gui.viewport;

import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.camera.OrthoCamera;
import com.wildrune.gui.viewport.camera.ViewportCamera;
import com.wildrune.util.MathHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 20/08/17
 */
public class OrthoViewport extends GLViewport {

    private Vector2f prevMousePosition;
    private OrthoCamera camera;

    public OrthoViewport(GLRenderer renderer, ViewportType type) {
        super(renderer, type);
    }

    @Override
    public void onInit() {
        reset();
    }

    @Override
    public void reset() {
        camera = new OrthoCamera();
        camera.setClipDistance(-getCameraDepth(), getCameraDepth());

        // ORTHO_FRONT is identity matrix
        switch (getType()) {
            case ORTHO_TOP:
                camera.rotate(90, MathHelper.UNIT_X);
                break;
            case ORTHO_SIDE:
                camera.rotate(-90, MathHelper.UNIT_Y);
                break;
        }
    }

    @Override
    public ViewportCamera getCamera() {
        return camera;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point mousePosition = e.getPoint();
        Vector2f currentMousePosition = new Vector2f(mousePosition.x, mousePosition.y);

        if (SwingUtilities.isRightMouseButton(e)) {
            SwingUtilities.invokeLater(() -> {
                Vector3f viewportPos = screenToWorld(currentMousePosition.x, currentMousePosition.y);
                Vector3f lastViewportPos = screenToWorld(prevMousePosition.x, prevMousePosition.y);

                camera.move(lastViewportPos.sub(viewportPos));

                prevMousePosition = currentMousePosition;
                repaint();
            });
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point mousePosition = e.getPoint();
        prevMousePosition = new Vector2f(mousePosition.x, mousePosition.y);
        requestFocus();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (SwingUtilities.isRightMouseButton(e)
                || SwingUtilities.isLeftMouseButton(e)
                || SwingUtilities.isMiddleMouseButton(e))
            return;

        double zoomFactor = camera.getZoom();
        zoomFactor = zoomFactor / (1.0 + -e.getWheelRotation() * e.getScrollAmount() * 0.14);

        if (zoomFactor > 0.98f && zoomFactor < 1.02f)
            zoomFactor = 1.0f;

        if (zoomFactor < 0.08f)
            zoomFactor = 0.08f;
        else if (zoomFactor > 72.0f)
            zoomFactor = 72.0f;

        camera.setZoom((float) zoomFactor);

        SwingUtilities.invokeLater(OrthoViewport.this::repaint);
    }
}

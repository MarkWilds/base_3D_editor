package com.wildrune.drawables;

import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.GLViewport;
import com.wildrune.gui.viewport.camera.ViewportCamera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static com.wildrune.util.MathHelper.*;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 15/08/17
 */
public class AxisIndicatorDrawable implements GLViewport.Drawable {

    private static final List<Vector3f> unitVectorList = new ArrayList<>();

    static {
        unitVectorList.add(UNIT_X);
        unitVectorList.add(UNIT_Y);
        unitVectorList.add(UNIT_Z);
    }

    private final float magicAxis = 16;
    private Matrix4f modelMatrix;

    public AxisIndicatorDrawable() {
        modelMatrix = new Matrix4f();
    }

    @Override
    public void render(GLViewport viewport, GLRenderer renderer) {
        ViewportCamera camera = viewport.getCamera();
        modelMatrix.identity().translate(magicAxis, camera.getViewportHeight() - magicAxis * 2, 0);

        renderer.setLineWidth(3);
        renderer.begin(modelMatrix, camera.getScreenProjectionMatrix());

        Vector3f originVector = new Vector3f(0, 0, -1f);
        unitVectorList.forEach(v -> {
            Vector3f transformedVector = new Vector3f(v).mul(magicAxis);
            camera.getViewMatrix().transformDirection(transformedVector);
            transformedVector.z = -1f;

            renderer.drawLine(originVector, transformedVector,
                    new Color(v.x, v.y, v.z),
                    GLRenderer.LINE_TYPE.NORMAL);
        });

        renderer.end();
        renderer.setLineWidth(1);
    }
}

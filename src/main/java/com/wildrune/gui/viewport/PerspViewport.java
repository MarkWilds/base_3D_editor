package com.wildrune.gui.viewport;

import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.camera.PerspCamera;
import com.wildrune.gui.viewport.camera.ViewportCamera;
import com.wildrune.util.MathHelper;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static com.wildrune.gui.viewport.GLViewport.ViewportType.PERSPECTIVE;
import static com.wildrune.util.MathHelper.*;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 20/08/17
 */
public class PerspViewport extends GLViewport {

    private static final List<Vector3f> unitVectorList = new ArrayList<>();

    static {
        unitVectorList.add(UNIT_X);
        unitVectorList.add(UNIT_Y);
        unitVectorList.add(UNIT_Z);
    }

    private PerspCamera camera;

    public PerspViewport(GLRenderer renderer) {
        super(renderer, PERSPECTIVE);
    }

    @Override
    public void onInit() {
        reset();
    }

    @Override
    public void reset() {
        camera = new PerspCamera();
        camera.move(new Vector3f(32, 64, -64));
        camera.lookAt(MathHelper.ZERO);
    }

    @Override
    public void onRender(GLRenderer renderer) {

        renderer.setLineWidth(8);
        renderer.begin(getCamera().getViewMatrix(), getCamera().getViewportProjectionMatrix());

        Vector3f originVector = new Vector3f(0, 0, 0);
        unitVectorList.forEach(v -> {
            renderer.drawLine(originVector, new Vector3f(v).mul(64),
                    new Color(v.x, v.y, v.z),
                    GLRenderer.LINE_TYPE.NORMAL);
        });

        renderer.end();
        renderer.setLineWidth(1);
    }

    @Override
    public ViewportCamera getCamera() {
        return camera;
    }
}

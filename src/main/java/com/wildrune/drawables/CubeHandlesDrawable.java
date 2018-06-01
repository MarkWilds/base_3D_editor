package com.wildrune.drawables;

import com.wildrune.geometry.AABB;
import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.GLViewport;
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
public class CubeHandlesDrawable implements GLViewport.Drawable {

    private AABB bounds;
    private float boxSize = 3;

    private List<Vector3f> boxMesh = new ArrayList<>();
    private List<Vector3f> boxMesh2 = new ArrayList<>();

    {
        boxMesh.add(new Vector3f(128, 128, 0));
        boxMesh.add(new Vector3f(512, 128, 0));
        boxMesh.add(new Vector3f(512, 0, 0));
        boxMesh.add(new Vector3f(128, 0, 0));

        boxMesh.add(new Vector3f(128, 128, 256));
        boxMesh.add(new Vector3f(512, 128, 256));
        boxMesh.add(new Vector3f(512, 0, 256));
        boxMesh.add(new Vector3f(128, 0, 256));
    }

    {
        boxMesh2.add(new Vector3f(128, 128, 128));
        boxMesh2.add(new Vector3f(512, 128, 128));
        boxMesh2.add(new Vector3f(512, 0, 128));
        boxMesh2.add(new Vector3f(128, 0, 128));

        boxMesh2.add(new Vector3f(128, 128, 256));
        boxMesh2.add(new Vector3f(512, 128, 256));
        boxMesh2.add(new Vector3f(512, 0, 256));
        boxMesh2.add(new Vector3f(128, 0, 256));
    }

    public CubeHandlesDrawable() {
        bounds = new AABB();
    }

    @Override
    public void render(GLViewport viewport, GLRenderer renderer) {
        bounds.reset();
        bounds.grow(boxMesh);
        List<Vector3f> cornerPoints = getCornerPoints(viewport, bounds);

        bounds.reset();
        bounds.grow(boxMesh2);
        List<Vector3f> cornerPoints2 = getCornerPoints(viewport, bounds);

        renderer.begin(IDENTITY, viewport.getCamera().getScreenProjectionMatrix());

        renderer.drawWiredPolygon(cornerPoints, Color.red, GLRenderer.LINE_TYPE.STRIPED);
        cornerPoints.forEach(p -> renderer.drawFilledPolygon(createScreenHandlePolygonVertices(p), Color.white));

        renderer.drawWiredPolygon(cornerPoints2, Color.yellow, GLRenderer.LINE_TYPE.STRIPED);
        cornerPoints2.forEach(p -> renderer.drawFilledPolygon(createScreenHandlePolygonVertices(p), Color.blue));

        renderer.end();
    }

    private List<Vector3f> getCornerPoints(final GLViewport viewport, final AABB bounds) {
        List<Vector3f> cornerPoints = new ArrayList<>();

        Vector3f screenCenter = viewport.worldToScreen(bounds.getCenter());
        Vector3f bottomLeft = viewport.worldToScreen(bounds.getMin());
        Vector3f topRight = viewport.worldToScreen(bounds.getMax());

        float halfWidth = (topRight.x - bottomLeft.x) * 0.5f;
        float halfHeight = (topRight.y - bottomLeft.y) * 0.5f;

        Vector3f up = new Vector3f(UNIT_Y).mul(halfHeight);
        Vector3f down = new Vector3f(UNIT_Y).negate().mul(halfHeight);
        Vector3f right = new Vector3f(UNIT_X).mul(halfWidth);
        Vector3f left = new Vector3f(UNIT_X).negate().mul(halfWidth);

        Vector3f topLeft = new Vector3f(screenCenter).add(up).add(left);
        Vector3f bottomRight = new Vector3f(screenCenter).add(down).add(right);

        cornerPoints.add(bottomLeft);
        cornerPoints.add(topLeft);
        cornerPoints.add(topRight);
        cornerPoints.add(bottomRight);

        return cornerPoints;
    }

    private List<Vector3f> createScreenHandlePolygonVertices(Vector3f screenPosition) {
        List<Vector3f> corners = new ArrayList<>();

        Vector3f topLeft = new Vector3f().add(UNIT_X).negate().add(UNIT_Y)
                .mul(boxSize)
                .add(screenPosition);
        Vector3f topRight = new Vector3f().add(UNIT_Y)
                .add(UNIT_X).mul(boxSize)
                .add(screenPosition);
        Vector3f bottomRight = new Vector3f().add(UNIT_Y).negate()
                .add(UNIT_X).mul(boxSize)
                .add(screenPosition);
        Vector3f bottomLeft = new Vector3f().add(UNIT_X).add(UNIT_Y).negate()
                .mul(boxSize)
                .add(screenPosition);

        corners.add(topLeft);
        corners.add(topRight);
        corners.add(bottomRight);
        corners.add(bottomLeft);

        return corners;
    }
}

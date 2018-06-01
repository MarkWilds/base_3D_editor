package com.wildrune.drawables;

import com.wildrune.geometry.AABB;
import com.wildrune.graphics.GLRenderer;
import com.wildrune.gui.viewport.GLViewport;
import com.wildrune.gui.viewport.camera.OrthoCamera;
import com.wildrune.gui.viewport.camera.ViewportCamera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.function.Consumer;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 14/08/17
 */
public class Grid implements GLViewport.Drawable {

    private final int maxGridSize;

    // set grid settings
    private Color minorGridColor;
    private Color majorGridColor;
    private Color originGridColor;
    private int hideLinesLower;
    private int majorLineEvery;
    private int gridSizeInPixels;

    public Grid(int maxGridSize) {
        // set grid settings
        this.maxGridSize = maxGridSize;
        minorGridColor = new Color(32, 32, 32);
        majorGridColor = new Color(64, 64, 64);
        originGridColor = new Color(192, 192, 192);
        hideLinesLower = 4;
        majorLineEvery = 8;
        gridSizeInPixels = 8;
    }

    private AABB calculateViewportScreenBounds(GLViewport viewport) {
        AABB viewportBounds = new AABB();
        ViewportCamera camera = viewport.getCamera();
        Vector3f[] worldExtends = new Vector3f[4];

        // get extends in camera space
        worldExtends[0] = viewport.screenToWorld(0, 0);
        worldExtends[1] = viewport.screenToWorld(camera.getViewportWidth(), 0);
        worldExtends[2] = viewport.screenToWorld(camera.getViewportWidth(), camera.getViewportHeight());
        worldExtends[3] = viewport.screenToWorld(0, camera.getViewportHeight());

        // convert to default space (x,y)
        for (int i = 0; i < 4; i++) {
            // transform to default space  world space
            Matrix4f toViewportMatrix = camera.getViewMatrix();
            viewportBounds.grow(toViewportMatrix.transformDirection(worldExtends[i]));
        }

        return viewportBounds;
    }

    private GridData calculateGridData(GLViewport viewport) {
        AABB bounds = calculateViewportScreenBounds(viewport);
        GridData gridData = new GridData();

        int gridSize = gridSizeInPixels;
        if(viewport.getType() != GLViewport.ViewportType.PERSPECTIVE) {
            OrthoCamera camera = (OrthoCamera) viewport.getCamera();

            // hide lines if grid is smaller than specified number
            while ((gridSize / camera.getZoom()) < hideLinesLower) {
                gridSize *= majorLineEvery;
            }
        }

        Vector3f min = bounds.getMin();
        Vector3f max = bounds.getMax();

        float gridWidth = max.x - min.x;
        float gridHeight = max.y - min.y;

        int gridCountX = (int) gridWidth / gridSize + 4;
        int gridCountY = (int) gridHeight / gridSize + 4;
        int gridStartX = (int) min.x / gridSize - 1;
        int gridStartY = (int) min.y / gridSize - 1;

        // Set line start and line end in world space coordinates
        float lineStartX = gridStartX * gridSize;
        float lineStartY = gridStartY * gridSize;
        float lineEndX = (gridStartX + (gridCountX - 1)) * gridSize;
        float lineEndY = (gridStartY + (gridCountY - 1)) * gridSize;

        // keep line start and line end inside the grid dimensions
        float gridDim = viewport.getCameraDepth();
        float finalLineStartX = (lineStartX < -gridDim) ? -gridDim : lineStartX;
        float finalLineStartY = (lineStartY < -gridDim) ? -gridDim : lineStartY;
        float finalLineEndX = (lineEndX > gridDim) ? gridDim : lineEndX;
        float finalLineEndY = (lineEndY > gridDim) ? gridDim : lineEndY;

        gridData.setGridSize(gridSize);
        gridData.setGridCount(new Vector2i(gridCountX, gridCountY));
        gridData.setGridStart(new Vector2i(gridStartX, gridStartY));
        gridData.setLineStart(new Vector2f(finalLineStartX, finalLineStartY));
        gridData.setLineEnd(new Vector2f(finalLineEndX, finalLineEndY));

        return gridData;
    }

    private void renderLines(float gridDim, int gridStart, int gridEnd, int gridSize, int lineType,
                             Consumer<Integer> lineRender) {
        for (int i = gridStart; i < gridEnd; ++i) {
            // skip lines that are out of bound
            if (i * gridSize < -gridDim || i * gridSize > gridDim)
                continue;

            // skip any line that don't match the line type we're adding
            if (lineType == 0 && (i == 0 || (i % majorLineEvery) == 0))
                continue;
            else if (lineType == 1 && (i == 0 || (i % majorLineEvery) != 0))
                continue;
            else if (lineType == 2 && i != 0)
                continue;

            lineRender.accept(i);
        }
    }

    @Override
    public void render(GLViewport viewport, GLRenderer renderer) {
        GridData gridData = calculateGridData(viewport);

        // we do no rotation as the grid is already in default(x,y) camera space
        Matrix4f translationViewMatrix = viewport.getCamera().getViewMatrix().setRotationXYZ(0, 0, 0);
        renderer.begin(translationViewMatrix, viewport.getCamera().getViewportProjectionMatrix());

        // the grid lines are ordered as minor, major, origin
        float grimDim = viewport.getCameraDepth();
        for (int lineType = 0; lineType < 3; lineType++) {
            Color lineColor = minorGridColor;
            if (lineType == 1)
                lineColor = majorGridColor;
            else if (lineType == 2)
                lineColor = originGridColor;

            final float lineDrawOrder = grimDim - (1 << lineType);
            final Color finalLinecolor = lineColor;
            final Vector2i gridStart = gridData.getGridStart();
            final Vector2i gridCount = gridData.getGridCount();
            final Vector2f lineStart = gridData.getLineStart();
            final Vector2f lineEnd = gridData.getLineEnd();

            // draw horizontal lines
            renderLines(grimDim, gridStart.y, gridStart.y + gridCount.y,
                    gridData.getGridSize(), lineType, index -> {
                        Vector3f from = new Vector3f(lineStart.x, (float) index * gridData.getGridSize(), lineDrawOrder);
                        Vector3f to = new Vector3f(lineEnd.x, (float) index * gridData.getGridSize(), lineDrawOrder);

                        renderer.drawLine(from, to, finalLinecolor, GLRenderer.LINE_TYPE.NORMAL);
                    });

            // draw vertical lines
            renderLines(grimDim, gridStart.x, gridStart.x + gridCount.x,
                    gridData.getGridSize(), lineType, index -> {
                        Vector3f from = new Vector3f((float) index * gridData.getGridSize(), lineStart.y, lineDrawOrder);
                        Vector3f to = new Vector3f((float) index * gridData.getGridSize(), lineEnd.y, lineDrawOrder);
                        renderer.drawLine(from, to, finalLinecolor, GLRenderer.LINE_TYPE.NORMAL);
                    });
        }

        renderer.end();
    }

    public void increaseGridSize() {
        gridSizeInPixels = gridSizeInPixels << 1;
        if (gridSizeInPixels == maxGridSize << 1) {
            gridSizeInPixels = maxGridSize;
        }
    }

    public void decreaseGridSize() {
        gridSizeInPixels = gridSizeInPixels >> 1;
        if (gridSizeInPixels == 0) {
            gridSizeInPixels = 1;
        }
    }

    private class GridData {

        private int gridSize;
        private Vector2i gridStart;
        private Vector2i gridCount;

        private Vector2f lineStart;
        private Vector2f lineEnd;

        int getGridSize() {
            return gridSize;
        }

        void setGridSize(int gridSize) {
            this.gridSize = gridSize;
        }

        Vector2i getGridStart() {
            return gridStart;
        }

        void setGridStart(Vector2i gridStart) {
            this.gridStart = gridStart;
        }

        Vector2i getGridCount() {
            return gridCount;
        }

        void setGridCount(Vector2i gridCount) {
            this.gridCount = gridCount;
        }

        Vector2f getLineStart() {
            return lineStart;
        }

        void setLineStart(Vector2f lineStart) {
            this.lineStart = lineStart;
        }

        Vector2f getLineEnd() {
            return lineEnd;
        }

        void setLineEnd(Vector2f lineEnd) {
            this.lineEnd = lineEnd;
        }
    }
}

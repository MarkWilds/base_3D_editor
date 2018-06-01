package com.wildrune.graphics;

import com.jogamp.opengl.GLAutoDrawable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 16/08/17
 */
public interface GLRenderer {

    enum LINE_TYPE {
        NORMAL,
        STRIPED
    }

    void setAutoDrawable(GLAutoDrawable drawable);

    void clearBackbuffers();

    void begin(Matrix4f modelView, Matrix4f projection);

    void end();

    void drawFilledPolygon(List<Vector3f> points, Color color);

    void drawWiredPolygon(List<Vector3f> points, Color color, LINE_TYPE lineType);

    void drawLine(Vector3f from, Vector3f to, Color color, LINE_TYPE lineType);

    void setLineWidth(int width);
}

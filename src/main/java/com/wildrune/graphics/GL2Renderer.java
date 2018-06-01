package com.wildrune.graphics;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 17/08/17
 */
public class GL2Renderer implements GLRenderer {

    private FloatBuffer matrixBuffer = Buffers.newDirectFloatBuffer(16);
    private List<RenderLineData> lineList;
    private List<RenderPolygonData> polygonList;
    private GL2 gl2;

    public GL2Renderer() {
        lineList = new ArrayList<>();
        polygonList = new ArrayList<>();
    }

    @Override
    public void setAutoDrawable(GLAutoDrawable drawable) {
        gl2 = drawable.getGL().getGL2();
    }

    @Override
    public void clearBackbuffers() {
        gl2.glEnable(GL.GL_DEPTH_TEST);
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void begin(Matrix4f modelView, Matrix4f projection) {
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        gl2.glLoadMatrixf(modelView.get(matrixBuffer));

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        gl2.glLoadMatrixf(projection.get(matrixBuffer));
    }

    @Override
    public void end() {
        if (!polygonList.isEmpty()) {
            polygonList.forEach(polygon -> {
                gl2.glBegin(gl2.GL_POLYGON);

                Color color = polygon.getColor();
                gl2.glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);

                polygon.getPoints().forEach(point -> gl2.glVertex3f(point.x, point.y, point.z));
                gl2.glEnd();
            });
            polygonList.clear();
        }

        if (!lineList.isEmpty()) {
            gl2.glBegin(GL.GL_LINES);

            lineList.forEach(line -> {
                Vector3f from = line.getFrom();
                Vector3f to = line.getTo();
                Color color = line.getColor();

                gl2.glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
                gl2.glVertex3f(from.x, from.y, from.z);
                gl2.glVertex3f(to.x, to.y, to.z);
            });

            gl2.glEnd();

            lineList.clear();
        }
    }

    @Override
    public void drawFilledPolygon(List<Vector3f> points, Color color) {
        RenderPolygonData polygonData = new RenderPolygonData(color, points);
        polygonList.add(polygonData);
    }

    @Override
    public void drawWiredPolygon(List<Vector3f> points, Color color, LINE_TYPE lineType) {
        int pointSize = points.size();
        for (int i = 0; i < pointSize; i++) {
            Vector3f from = points.get(i);
            Vector3f to = points.get((i + 1) % pointSize);

            drawLine(from, to, color, lineType);
        }
    }

    @Override
    public void drawLine(Vector3f from, Vector3f to, Color color, LINE_TYPE lineType) {

        switch (lineType) {
            case NORMAL:
                drawLineNormal(from, to, color);
                break;
            case STRIPED:
                drawLineStriped(from, to, color);
                break;
        }
    }

    private void drawLineNormal(Vector3f from, Vector3f to, Color color) {
        lineList.add(new RenderLineData(from, to, color));
    }

    private void drawLineStriped(Vector3f from, Vector3f to, Color color) {
        float lineLength = 6;
        float length = new Vector3f().add(to).sub(from).length();
        int linecount = (int) (length / lineLength);

        Vector3f direction = new Vector3f().add(to).sub(from).normalize();
        Vector3f start = new Vector3f(from);

        for (int i = 0; i < linecount; i++) {
            Vector3f end = new Vector3f(direction).mul(lineLength).add(start);

            // if this is an even line continue but still calculate new endpoint
            if (i % 2 == 1) {
                start = end;
                continue;
            }

            drawLineNormal(start, end, color);

            start = end;
        }
    }

    @Override
    public void setLineWidth(int width) {
        gl2.glLineWidth(width);
    }
}

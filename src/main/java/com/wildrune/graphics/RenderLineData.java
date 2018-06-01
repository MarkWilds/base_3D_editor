package com.wildrune.graphics;

import org.joml.Vector3f;

import java.awt.Color;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 17/08/17
 */
class RenderLineData {
    private Vector3f from;
    private Vector3f to;
    private Color color;

    RenderLineData(Vector3f from, Vector3f to, Color color) {
        this.from = new Vector3f(from);
        this.to = new Vector3f(to);
        this.color = new Color(color.getRGB());
    }

    Vector3f getFrom() {
        return from;
    }

    Vector3f getTo() {
        return to;
    }

    Color getColor() {
        return color;
    }
}

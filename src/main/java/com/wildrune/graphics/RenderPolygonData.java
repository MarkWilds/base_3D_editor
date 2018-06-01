package com.wildrune.graphics;

import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 17/08/17
 */
class RenderPolygonData {
    private Color color;
    private List<Vector3f> points;

    RenderPolygonData(Color color, List<Vector3f> points) {
        if(points == null) {
            this.points = new ArrayList<>();
        } else {
            this.points = new ArrayList<>(points);
        }

        this.color = new Color(color.getRGB());
    }

    Color getColor() {
        return color;
    }

    List<Vector3f> getPoints() {
        return points;
    }
}

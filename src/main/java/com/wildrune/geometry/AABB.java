package com.wildrune.geometry;

import org.joml.Vector3f;

import java.util.Collection;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 15/08/17
 */
public class AABB {

    private final Vector3f min;
    private final Vector3f max;

    public AABB() {
        min = new Vector3f();
        max = new Vector3f();
        reset();
    }

    public void reset() {
        min.x = Float.MAX_VALUE;
        min.y = Float.MAX_VALUE;
        min.z = Float.MAX_VALUE;
        max.x = -Float.MAX_VALUE;
        max.y = -Float.MAX_VALUE;
        max.z = -Float.MAX_VALUE;
    }

    public void grow(Vector3f vector) {
        if (vector.x < min.x) min.x = vector.x;
        if (vector.y < min.y) min.y = vector.y;
        if (vector.z < min.z) min.z = vector.z;
        if (vector.x > max.x) max.x = vector.x;
        if (vector.y > max.y) max.y = vector.y;
        if (vector.z > max.z) max.z = vector.z;
    }

    public void grow(Collection<Vector3f> vectors) {
        vectors.forEach(this::grow);
    }

    /***
     * If the box has volume in any of the axis it has 2D volume
     * @return true if it has 2D volume
     */
    public boolean hasVolume2D()
    {
        // check if the brush has valid bounds
        float absX = Math.abs(min.x - min.x);
        float absY = Math.abs(min.y - min.y);
        float absZ = Math.abs(min.z - min.z);

        return absX > 0 || absY > 0 || absZ > 0;
    }

    /***
     *  If the box has volume in all of the axis it has 3D volume
     * @return true if it has 3D volume
     */
    public boolean hasVolume3D()
    {
        // check if the brush has valid bounds
        float absX = Math.abs(min.x - max.x);
        float absY = Math.abs(min.y - max.y);
        float absZ = Math.abs(min.z - max.z);

        return absX > 0 && absY > 0 && absZ > 0;
    }

    public Vector3f getCenter() {
        return new Vector3f().add(min).add(max).mul(0.5f);
    }

    public Vector3f getMin() {
        return new Vector3f(min);
    }

    public Vector3f getMax() {
        return new Vector3f(max);
    }
}

package com.wildrune.util;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 17/08/17
 */
public class MathHelper {

    public static final Vector3f ZERO = new Vector3f(0, 0, 0);
    public static final Vector3f UNIT_X = new Vector3f(1, 0, 0);
    public static final Vector3f UNIT_Y = new Vector3f(0, 1, 0);
    public static final Vector3f UNIT_Z = new Vector3f(0, 0, 1);

    public static final Matrix4f IDENTITY = new Matrix4f().identity();
}

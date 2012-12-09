/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

import com.ciebiada.reddot.primitive.Primitive;

public class Ray {

    public final Vec orig, dir;

    /**
     * intersection optimizations
     */
    public final Vec dirInv;
    public final int[] sign;

    public Ray(Vec orig, Vec dir) {
        this.orig = orig;
        this.dir = dir;

        dirInv = new Vec(1 / dir.x, 1 / dir.y, 1 / dir.z);
        sign = new int[]{
                (dirInv.x < 0) ? 1 : 0,
                (dirInv.y < 0) ? 1 : 0,
                (dirInv.z < 0) ? 1 : 0
        };
    }
}

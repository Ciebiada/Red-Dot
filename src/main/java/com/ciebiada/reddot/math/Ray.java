/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

import com.ciebiada.reddot.primitive.Primitive;

public class Ray {

    public Vec orig, dir;

    /**
     * intersection optimizations
     */
    public Vec dirInv;
    public int[] sign = new int[3];

    /**
     * hit data
     */
    public float t, cosI;
    public Vec ip, nor;
    public Primitive hit;

    public Ray(Vec orig, Vec dir) {
        setRay(orig, dir);
    }

    public void setRay(Vec orig, Vec dir) {
        this.orig = orig;
        this.dir = dir;

        dirInv = new Vec(1 / dir.x, 1 / dir.y, 1 / dir.z);
        sign[0] = (dirInv.x < 0) ? 1 : 0;
        sign[1] = (dirInv.y < 0) ? 1 : 0;
        sign[2] = (dirInv.z < 0) ? 1 : 0;

        t = Float.POSITIVE_INFINITY;
    }
}

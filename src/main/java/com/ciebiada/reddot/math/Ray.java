/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

import com.ciebiada.reddot.geometry.Triangle;

public class Ray {

    public Vec orig, dir;

    public float dirxInv, diryInv, dirzInv;

    public float tmin, tmax;
    public Vec nor;
    public Triangle tri;

    public Ray(Vec orig, Vec dir) {
        this.orig = orig;
        this.dir = dir;
        tmin = 1e-5f;
        tmax = Float.POSITIVE_INFINITY;

        dirxInv = 1 / dir.x;
        diryInv = 1 / dir.y;
        dirzInv = 1 / dir.z;
    }

    public void set(Vec orig, Vec dir) {
        this.orig = orig;
        this.dir = dir;
        tmin = 1e-5f;
        tmax = Float.POSITIVE_INFINITY;

        dirxInv = 1 / dir.x;
        diryInv = 1 / dir.y;
        dirzInv = 1 / dir.z;
    }
}

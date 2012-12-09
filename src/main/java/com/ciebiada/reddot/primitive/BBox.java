/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;

public class BBox {

    private final Vec[] bounds;

    public BBox(Vec min, Vec max) {
        bounds = new Vec[] {min, max};
    }

    public Vec getMin() {
        return bounds[0];
    }

    public Vec getMax() {
        return bounds[1];
    }

    public boolean hit(Ray ray, double tmax) {
        double t1, t2, tymin, tymax, tzmin, tzmax;

        t1 = (bounds[ray.sign[0]].x - ray.orig.x) * ray.dirInv.x;
        t2 = (bounds[1 - ray.sign[0]].x - ray.orig.x) * ray.dirInv.x;

        tymin = (bounds[ray.sign[1]].y - ray.orig.y) * ray.dirInv.y;
        tymax = (bounds[1 - ray.sign[1]].y - ray.orig.y) * ray.dirInv.y;

        if (t1 > tymax || tymin > t2)
            return false;
        if (tymin > t1)
            t1 = tymin;
        if (tymax < t2)
            t2 = tymax;

        tzmin = (bounds[ray.sign[2]].z - ray.orig.z) * ray.dirInv.z;
        tzmax = (bounds[1 - ray.sign[2]].z - ray.orig.z) * ray.dirInv.z;

        if (t1 > tzmax || tzmin > t2)
            return false;
        if (tzmin > t1)
            t1 = tzmin;
        if (tzmax < t2)
            t2 = tzmax;

        return (t1 < tmax && t2 > 0);
    }
}

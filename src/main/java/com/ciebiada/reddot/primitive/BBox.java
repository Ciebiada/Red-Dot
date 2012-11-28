/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;

public class BBox {

    private Vec[] bounds;

    public BBox() {
        bounds = new Vec[2];
        bounds[0] = new Vec(Float.POSITIVE_INFINITY);
        bounds[1] = new Vec(Float.NEGATIVE_INFINITY);
    }

    public Vec getMin() {
        return bounds[0];
    }

    public Vec getMax() {
        return bounds[1];
    }

    public void fit(Vec v) {
        if (v.x > bounds[1].x)
            bounds[1].x = v.x;
        if (v.y > bounds[1].y)
            bounds[1].y = v.y;
        if (v.z > bounds[1].z)
            bounds[1].z = v.z;

        if (v.x < bounds[0].x)
            bounds[0].x = v.x;
        if (v.y < bounds[0].y)
            bounds[0].y = v.y;
        if (v.z < bounds[0].z)
            bounds[0].z = v.z;
    }

	public boolean hit(Ray ray) {
        float tmin, tmax, tymin, tymax, tzmin, tzmax;

        tmin = (bounds[ray.sign[0]].x - ray.orig.x) * ray.dirInv.x;
        tmax = (bounds[1 - ray.sign[0]].x - ray.orig.x) * ray.dirInv.x;

        tymin = (bounds[ray.sign[1]].y - ray.orig.y) * ray.dirInv.y;
        tymax = (bounds[1 - ray.sign[1]].y - ray.orig.y) * ray.dirInv.y;

        if (tmin > tymax || tymin > tmax)
            return false;
        if (tymin > tmin)
            tmin = tymin;
        if (tymax < tmax)
            tmax = tymax;

        tzmin = (bounds[ray.sign[2]].z - ray.orig.z) * ray.dirInv.z;
        tzmax = (bounds[1 - ray.sign[2]].z - ray.orig.z) * ray.dirInv.z;

        if (tmin > tzmax || tzmin > tmax)
            return false;
        if (tzmin > tmin)
            tmin = tzmin;
        if (tzmax < tmax)
            tmax = tzmax;

        return (tmin < ray.t && tmax > 0);
	}
}

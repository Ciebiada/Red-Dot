package com.ciebiada.reddot.geometry;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;

public class BBox {

    private Vec min, max;

    public BBox(Vec min, Vec max) {
        this.min = min;
        this.max = max;
    }

    public boolean intersect(Ray ray) {
        float tmin = ray.tmin;
        float tmax = ray.tmax;

        float t1 = (min.x - ray.orig.x) * ray.dirxInv;
        float t2 = (max.x - ray.orig.x) * ray.dirxInv;
        if (ray.dirxInv > 0) {
            if (t1 > tmin)
                tmin = t1;
            if (t2 < tmax)
                tmax = t2;
        } else {
            if (t2 > tmin)
                tmin = t2;
            if (t1 < tmax)
                tmax = t1;
        }
        if (tmin > tmax)
            return false;

        t1 = (min.y - ray.orig.y) * ray.diryInv;
        t2 = (max.y - ray.orig.y) * ray.diryInv;
        if (ray.diryInv > 0) {
            if (t1 > tmin)
                tmin = t1;
            if (t2 < tmax)
                tmax = t2;
        } else {
            if (t2 > tmin)
                tmin = t2;
            if (t1 < tmax)
                tmax = t1;
        }
        if (tmin > tmax)
            return false;

        t1 = (min.z - ray.orig.z) * ray.dirzInv;
        t2 = (max.z - ray.orig.z) * ray.dirzInv;
        if (ray.dirzInv > 0) {
            if (t1 > tmin)
                tmin = t1;
            if (t2 < tmax)
                tmax = t2;
        } else {
            if (t2 > tmin)
                tmin = t2;
            if (t1 < tmax)
                tmax = t1;
        }

        if (tmin > tmax)
            return false;

        ray.tmin = tmin;
        ray.tmax = tmax;

        return true;
    }
}

package com.ciebiada.reddot.geometry;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;

public class Triangle {

    private Material material;

    private Vec nor;

    private float d, d1, d2;
    private Vec n1, n2;

    public Triangle(Vec a, Vec b, Vec c, Material material) {
        this.material = material;

        Vec nor = b.sub(a).cross(c.sub(a)).norm();

        d = a.dot(nor);
        Vec n = b.sub(a).cross(c.sub(a));
        n1 = c.sub(a).cross(n).div(n.dot(n));
        n2 = n.cross(b.sub(a)).div(n.dot(n));
        d1 = -n1.dot(a);
        d2 = -n2.dot(a);
    }

    public Material getMat() {
        return material;
    }

    public Vec getNor() {
        return nor;
    }

    public boolean rayIntersection(Ray ray) {
        float det = ray.dir.dot(nor);
        float t = (d - ray.orig.dot(nor));

        if (t * (ray.tmax * det - t) > 0) {
            Vec p = ray.orig.mul(det).add(ray.dir.mul(t));

            float u = n1.dot(p) + det * d1;
            if (u * (det - u) > 0) {
                float v = n2.dot(p) + det * d2;
                if (v * (det - u - v) > 0) {
                    ray.tmax = t / det;
                    ray.nor = getNor();
                    ray.tri = this;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean shadowRayIntersection(Ray ray) {
        float det = ray.dir.dot(nor);
        float t = (d - ray.orig.dot(nor));

        if (t * (ray.tmax * det - t) > 0) {
            Vec p = ray.orig.mul(det).add(ray.dir.mul(t));

            float u = n1.dot(p) + det * d1;
            if (u * (det - u) > 0) {
                float v = n2.dot(p) + det * d2;
                return (v * (det - u - v) > 0);
            }
        }

        return false;
    }
}

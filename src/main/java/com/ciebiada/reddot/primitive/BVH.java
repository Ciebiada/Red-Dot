/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Sample;
import com.ciebiada.reddot.math.Vec;

public class BVH extends Primitive {

    private final BBox bbox;
    private final Primitive left, right;

    private BVH(BBox bbox, Primitive left, Primitive right) {
        this.bbox = bbox;
        this.left = left;
        this.right = right;
    }

    public static Primitive build(Primitive[] primitives, int l, int r) {
        if (r - l == 1)
            return primitives[l];

        BBox bbox = getBBox(primitives, l, r);

        Vec extent = bbox.getMax().sub(bbox.getMin());
        int axis = (extent.x > extent.y) ? ((extent.x > extent.z) ? 0 : 2) : ((extent.y > extent.z) ? 1 : 2);
        double pivot = (bbox.getMin().get(axis) + bbox.getMax().get(axis)) * 0.5f;

        int moved = l;

        for (int i = l; i < r; ++i) {
            double cen = (primitives[i].getMin().get(axis) + primitives[i].getMax().get(axis)) * 0.5f;
            if (cen <= pivot) {
                Primitive tmp = primitives[i];
                primitives[i] = primitives[moved];
                primitives[moved] = tmp;
                ++moved;
            }
        }

        if (moved == l || moved == r)
            moved = (r + l) / 2;

        return new BVH(bbox, build(primitives, l, moved), build(primitives, moved, r));
    }

    private static BBox getBBox(Primitive[] primitives, int l, int r) {
        double minx = Double.POSITIVE_INFINITY;
        double miny = Double.POSITIVE_INFINITY;
        double minz = Double.POSITIVE_INFINITY;
        double maxx = Double.NEGATIVE_INFINITY;
        double maxy = Double.NEGATIVE_INFINITY;
        double maxz = Double.NEGATIVE_INFINITY;

        for (int i = l; i < r; ++i) {
            Vec min = primitives[i].getMin();
            if (min.x < minx)
                minx = min.x;
            if (min.y < miny)
                miny = min.y;
            if (min.z < minz)
                minz = min.z;

            Vec max = primitives[i].getMax();
            if (max.x > maxx)
                maxx = max.x;
            if (max.y > maxy)
                maxy = max.y;
            if (max.z > maxz)
                maxz = max.z;
        }

        return new BBox(new Vec(minx, miny, minz).sub(new Vec(1e-5)), new Vec(maxx, maxy, maxz).add(new Vec(1e-5)));
    }

    @Override
    public Vec[] sample(Sample sample) {
        return null;
    }

    @Override
    public boolean hit(Ray ray, HitData hit) {
        if (!bbox.hit(ray, hit.t))
            return false;

        boolean hit1 = right.hit(ray, hit);
        boolean hit2 = left.hit(ray, hit);

        return (hit1 || hit2);
    }

    @Override
    public boolean shadowHit(Ray ray, double tmax) {
        if (!bbox.hit(ray, tmax))
            return false;

        if (right.shadowHit(ray, tmax))
            return true;

        return left.shadowHit(ray, tmax);
    }

    /**
     * following methods are not important as we don't want to display the node directly
     */

    @Override
    public Material getMat() {
        return null;
    }

    @Override
    public Vec getMin() {
        return bbox.getMin();
    }

    @Override
    public Vec getMax() {
        return bbox.getMax();
    }

    @Override
    public double getArea() {
        return 0;
    }
}

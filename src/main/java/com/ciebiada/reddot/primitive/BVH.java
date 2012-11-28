/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public class BVH extends Primitive {

    private BBox bbox;
    private Primitive left, right;

    private BVH(BBox bbox, Primitive left, Primitive right) {
        this.bbox = bbox;
        this.left = left;
        this.right = right;
    }

    public static Primitive build(Primitive[] primitives, int l, int r) {
        if (r - l == 1)
            return primitives[l];

        BBox bbox = new BBox();

        for (int i = l; i < r; ++i) {
            bbox.fit(primitives[i].getMin());
            bbox.fit(primitives[i].getMax());
        }

        bbox.getMin().subSet(new Vec(1e-4f));
        bbox.getMax().addSet(new Vec(1e-4f));

        Vec extent = bbox.getMax().sub(bbox.getMin());
        int axis = (extent.x > extent.y) ? ((extent.x > extent.z) ? 0 : 2) : ((extent.y > extent.z) ? 1 : 2);
        float pivot = (bbox.getMin().get(axis) + bbox.getMax().get(axis)) * 0.5f;

        int moved = l;

        for (int i = l; i < r; ++i) {
            float cen = (primitives[i].getMin().get(axis) + primitives[i].getMax().get(axis)) * 0.5f;
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

    @Override
    public boolean hit(Ray ray) {
        if (!bbox.hit(ray))
            return false;

        boolean hit1 = right.hit(ray);
        boolean hit2 = left.hit(ray);

        return (hit1 || hit2);
    }

    @Override
    public boolean shadowHit(Ray ray) {
        if (!bbox.hit(ray))
            return false;

        if (right.shadowHit(ray))
            return true;

        return left.shadowHit(ray);
    }

    /**
     * following methods are not important as we don't want to display the node directly
     */

    @Override
    public Material getMat() {
        return null;
    }

    @Override
    public Vec[] sample(float[] sample) {
        return null;
    }

    @Override
    public Vec getMin() {
        return null;
    }

    @Override
    public Vec getMax() {
        return null;
    }

    @Override
    public float getArea() {
        return 0;
    }
}

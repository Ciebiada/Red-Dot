/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.primitive.HitData;
import com.ciebiada.reddot.sampler.Sampler;

public class Diffuse extends Material {

    private final Col diffuse;

    public Diffuse(Col diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public Brdf getBrdf(Ray ray, HitData hit, boolean adjoint, Sampler sampler) {
        if (adjoint) {
            Vec ns = hit.nors;
            Vec ng = hit.norg;
            Vec dir = getCosineWeightedDir(hit.basis, sampler.getSample());

            double scale = Math.abs(ray.dir.dot(ns)) / Math.abs(ray.dir.dot(ng));//ng.dot(dir) / -ray.dir.dot(ng);

            if (dir.dot(ng) * dir.dot(ns) <= 0 || ray.dir.dot(ng) * ray.dir.dot(ns) <= 0)
                return new Brdf(true, true);

            return new Brdf(diffuse.mul(scale), dir, true);
        } else {
            return new Brdf(diffuse, getCosineWeightedDir(hit.basis, sampler.getSample()), true);
        }
    }

    public static Vec getCosineWeightedDir(OBasis basis, Sample sample) {
        double phi = 2 * Math.PI * sample.getX();
        double r2 = sample.getY();
        double r2s = Math.sqrt(r2);

        return basis.transform(r2s * Math.cos(phi), r2s * Math.sin(phi), Math.sqrt(1 - r2));
    }
}

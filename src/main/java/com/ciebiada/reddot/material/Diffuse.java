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
            Vec ns = hit.nors;
            Vec ng = hit.norg;

            Vec wi = getCosineWeightedDir(hit.basis, sampler.getSample());

            Vec wo = ray.dir.mul(-1);

//            if (wi.dot(ng) * wi.dot(ns) <= 0 || wo.dot(ng) * wo.dot(ns) <= 0)
//                return new Brdf(false, true);


            if (wi.dot(ng) * wi.dot(ns) <= 0 || wo.dot(ng) * wo.dot(ns) <= 0)
                return new Brdf(true, true);
//            if (ray.dir.mul(-1).dot(hit.norg) * wo.dot(hit.norg) < 0)
//                return new Brdf(false, true);

        if (wo.dot(ng) * wi.dot(ng) <= 0)
                return new Brdf(false, true);


        if (adjoint) {
//        double scale = Math.abs(ray.dir.dot(ns)) / Math.abs(ray.dir.dot(ng));
//            double scale = Math.abs(ns.dot(wi) * ng.dot(wo)) / (ns.dot(wo) * ng.dot(wi));
//            scale = Math.max(0, scale);

//            double scale = Math.abs(wo.dot(ng)) / Math.abs(wi.dot(ng);//Math.abs(ray.dir.dot(ns)) / Math.abs(ray.dir.dot(ng));//ng.dot(dir) / -ray.dir.dot(ng);
//            if (wi.dot(ng) <= 1e-2)
//                return new Brdf(false, true);

            double scale = Math.abs(wo.dot(ns)) / Math.abs(wo.dot(ng));
            if (wo.dot(ng) < 0.01 || wo.dot(ns) < 0.01)
                scale = 0;

            if (wo.dot(ng) > 10 || wo.dot(ns) > 10)
                scale = 1;
//            if (Double.isNaN(scale) || Double.isInfinite(scale))
//                scale = 0;
            return new Brdf(diffuse.mul(scale), wi, true);
        } else {
            double scale = Math.abs(wi.dot(ns)) / Math.abs(wi.dot(ng));
            return new Brdf(diffuse.mul(scale), wi, true);
        }
    }

    public static Vec getCosineWeightedDir(OBasis basis, Sample sample) {
        double phi = 2 * Math.PI * sample.getX();
        double r2 = sample.getY();
        double r2s = Math.sqrt(r2);

        return basis.transform(r2s * Math.cos(phi), r2s * Math.sin(phi), Math.sqrt(1 - r2));
    }
}

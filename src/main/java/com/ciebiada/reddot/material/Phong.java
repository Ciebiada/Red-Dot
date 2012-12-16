/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.primitive.HitData;
import com.ciebiada.reddot.sampler.Sampler;

public class Phong extends Material {

    private final Col diffuse, specular;
    private final double n;

    public Phong(Col diffuse, Col specular, double n) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.n = n;
    }

    @Override
    public Brdf getBrdf(Ray ray, HitData hit, boolean adjoint, Sampler sampler) {
        Col fresnel = fresnelApprox(-ray.dir.dot(hit.basis.getW()));

//        double r = sampler.get1dSample();
//        double p = (fresnel.r + fresnel.g + fresnel.b) / 3;
//
        double p = 1;
//        if (r < p) {
            Vec inc = getPhongDirection(ray, hit.basis, sampler.getSample());
            if (inc.dot(hit.basis.getW()) > 0)
                return new Brdf(diffuse.mul(fresnel).div(p), inc, false);
            else
                return new Brdf(false, true);
//        } else {
//            double norm = (n + 2) / (n + 1) * hit.cosI;
//            return new Brdf(diffuse.mul(new Col(1).sub(fresnel)).mul(norm / (1 - p)),
//                    Diffuse.getCosineWeightedDir(hit.basis, sampler.getSample()), true);
//        }
    }

    private Vec getPhongDirection(Ray ray, OBasis basis, Sample sample) {
        double phi = 2 * Math.PI * sample.getX();
        double cosT = Math.pow(sample.getY(), 1 / (n + 1));
        double sinT = Math.sqrt(1 - cosT * cosT);

        Vec perfect = ray.dir.sub(basis.getW().mul(2.0f * ray.dir.dot(basis.getW())));

        return new OBasis(perfect).transform(sinT * Math.cos(phi), sinT * Math.sin(phi), cosT);
    }

    private Col fresnelApprox(double u) {
        double x = (1 - u);
        return specular.add(new Col(1).sub(specular).mul(x * x * x * x * x));
    }
}

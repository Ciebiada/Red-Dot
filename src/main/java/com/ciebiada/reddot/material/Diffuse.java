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
    public Brdf getBrdf(Ray ray, HitData hit, Sampler sampler) {
        return new Brdf(diffuse, getDiffuseDirection(ray, hit.basis, sampler.getSample()));
    }

    static Vec getDiffuseDirection(Ray ray, OBasis basis, double[] sample) {
        double phi = 2 * Math.PI * sample[0];
        double r2 = sample[1];
        double r2s = Math.sqrt(r2);

        return basis.transform(r2s * Math.cos(phi), r2s * Math.sin(phi), Math.sqrt(1 - sample[1]));
    }
}

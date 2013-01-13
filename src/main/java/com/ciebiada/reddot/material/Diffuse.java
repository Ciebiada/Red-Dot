/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.sampler.Sampler;

public class Diffuse extends Material {

    private Col diffuse;

    public Diffuse(Col diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public Brdf getBrdf(Ray ray, Sampler sampler) {
            return new Brdf(diffuse, getCosineWeightedDir(ray.nor, sampler.getSample()), true);
    }

    private Vec getCosineWeightedDir(Vec nor, Sample sample) {
        float phi = 2 * (float) Math.PI * sample.x;
        float r2 = sample.y;
        float r2s = (float) Math.sqrt(r2);

        OBasis basis = new OBasis(nor);

        return basis.transform(
                r2s * (float) Math.cos(phi),
                r2s * (float) Math.sin(phi),
                (float) Math.sqrt(1 - r2));
    }
}

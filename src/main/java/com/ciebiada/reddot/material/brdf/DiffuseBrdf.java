/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material.brdf;

import com.ciebiada.reddot.camera.OrthoBasis;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Utils;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public class DiffuseBrdf extends Brdf {

    public final static Brdf INVALID = new DiffuseBrdf(Col.BLACK);

    private Col diffuse;

    public DiffuseBrdf(Col diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public Vec sampleDirection(Ray ray, float[] sample) {
        float phi = 2 * (float) Math.PI * sample[0];
        float r2 = sample[1];
        float r2s = (float) Math.sqrt(r2);

        Vec dir = new Vec(
                r2s * (float) Math.cos(phi),
                r2s * (float) Math.sin(phi),
                (float) Math.sqrt(1 - sample[1])
        );

        return new OrthoBasis(ray.nor).transform(dir);
    }

    @Override
    public Col evaluate(Ray ray, Vec out) {
        return diffuse.mul(1 / Utils.PI);
    }

    @Override
    public float getPdf(Ray ray, Vec out) {
        return ray.cosI / Utils.PI;
    }

    @Override
    public boolean isDiffuse() {
        return true;
    }
}

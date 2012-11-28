/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.material.brdf.Brdf;
import com.ciebiada.reddot.material.brdf.DiffuseBrdf;
import com.ciebiada.reddot.material.brdf.PhongBrdf;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.sampler.Sampler;

public class MixedPhong extends Material {

    private Col diffuse;
    private Col specular;
    private int n;

    private float p;

    public MixedPhong(Col diffuse, Col specular, int n) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.n = n;

        p = (specular.r + specular.g + specular.b) / 3;
    }

    @Override
    public Brdf getBrdf(Ray ray, Sampler sampler) {
        Brdf brdf;

        float x = 1 - ray.cosI;
        Col schlick = new Col(1).sub(specular).mul(x * x * x * x * x).add(specular);

        if (sampler.get1dSample() < p) {
            brdf = new PhongBrdf(schlick.mul(1 / p), n);
        } else {
            brdf = new DiffuseBrdf(new Col(1).sub(schlick).mul(diffuse).div(1 - p));
        }

        return brdf;
    }
}

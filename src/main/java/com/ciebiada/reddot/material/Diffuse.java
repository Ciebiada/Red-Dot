/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.material.brdf.Brdf;
import com.ciebiada.reddot.material.brdf.DiffuseBrdf;
import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.sampler.Sampler;

public class Diffuse extends Material {

    private Brdf brdf;

    public Diffuse(Col diffuse) {
        brdf = new DiffuseBrdf(diffuse);
    }

    @Override
    public Brdf getBrdf(Ray ray, Sampler sampler) {
        return brdf;
    }
}

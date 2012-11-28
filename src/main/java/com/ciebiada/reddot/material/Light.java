/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.material.brdf.AbsorpBrdf;
import com.ciebiada.reddot.material.brdf.Brdf;
import com.ciebiada.reddot.material.brdf.DiffuseBrdf;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.sampler.Sampler;

public class Light extends Material {

    private Col emittance;

    private Brdf brdf;

    public Light(Col emittance) {
        this.emittance = emittance;
        brdf = new AbsorpBrdf();
    }

    @Override
    public Brdf getBrdf(Ray ray, Sampler sampler) {
        return brdf;
    }

    @Override
    public Col getEmittance() {
        return emittance;
    }

    @Override
    public boolean isEmissive() {
        return true;
    }
}

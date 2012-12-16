/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.primitive.HitData;
import com.ciebiada.reddot.sampler.Sampler;

public class Light extends Material {

    private Col emittance;

    public Light(Col emittance) {
        this.emittance = emittance;
    }

    @Override
    public Brdf getBrdf(Ray ray, HitData hit, boolean adjoint, Sampler sampler) {
        return new Brdf(emittance, null, true, true);
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

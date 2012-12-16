/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.primitive.HitData;
import com.ciebiada.reddot.sampler.Sampler;

public abstract class Material {

    public abstract Brdf getBrdf(Ray ray, HitData hit, boolean adjoint, Sampler sampler);

    public Col getEmittance() {
        return Col.BLACK;
    }

    public boolean isEmissive() {
        return false;
    }
}

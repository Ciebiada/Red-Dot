/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material.brdf;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public abstract class Brdf {

    public abstract Vec sampleDirection(Ray ray, float[] sample);

    public abstract Col evaluate(Ray ray, Vec out);

    public abstract float getPdf(Ray ray, Vec out);

    public boolean isSpecular() {
        return false;
    }

    public boolean isDiffuse() {
        return false;
    }
}

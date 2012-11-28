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

public class AbsorpBrdf extends Brdf {

    @Override
    public Vec sampleDirection(Ray ray, float[] sample) {
        return Vec.INVALID;
    }

    @Override
    public Col evaluate(Ray ray, Vec out) {
        return Col.BLACK;
    }

    @Override
    public float getPdf(Ray ray, Vec out) {
        return 1;
    }
}

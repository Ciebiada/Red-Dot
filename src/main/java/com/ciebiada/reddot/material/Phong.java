/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.material.brdf.Brdf;
import com.ciebiada.reddot.material.brdf.PhongBrdf;
import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.sampler.Sampler;

public class Phong extends Material {

    private Brdf brdf;

    public Phong(Col specular, int n) {
        brdf = new PhongBrdf(specular, n);
    }

    @Override
    public Brdf getBrdf(Ray ray, Sampler sampler) {
        return brdf;
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Vec;

public class Brdf {

    public Col scale;
    public Vec dir;
    public boolean diffuse;

    public Brdf(Col scale, Vec dir, boolean diffuse) {
        this.scale = scale;
        this.dir = dir;
        this.diffuse = diffuse;
    }

    public boolean isAbsorptive() {
        return false;
    }
}

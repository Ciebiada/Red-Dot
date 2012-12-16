/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Vec;

public final class Brdf {

    private final Col scale;
    private final Vec dir;
    private final boolean diffuse;
    private final boolean absorptive;

    public Brdf(Col scale, Vec dir, boolean diffuse, boolean absorptive) {
        this.scale = scale;
        this.dir = dir;
        this.diffuse = diffuse;
        this.absorptive = absorptive;
    }

    public Brdf(Col scale, Vec dir, boolean diffuse) {
        this(scale, dir, diffuse, false);
    }

    public Brdf(boolean diffuse, boolean absorptive) {
        this(null, null, diffuse, absorptive);
    }

    public Col getScale() {
        return scale;
    }

    public Vec getDir() {
        return dir;
    }

    public boolean isDiffuse() {
        return diffuse;
    }

    public boolean isAbsorptive() {
        return absorptive;
    }
}

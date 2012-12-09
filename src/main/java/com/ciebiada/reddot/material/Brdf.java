/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Vec;

public final class Brdf {

    private final Col scale;
    private final Vec inc;
    private final boolean absorptive;

    public Brdf(Col scale, Vec inc) {
        this(scale, inc, false);
    }

    public Brdf() {
        this(null, null, true);
    }

    public Brdf(Col scale, Vec inc, boolean absorptive) {
        this.scale = scale;
        this.inc = inc;
        this.absorptive = absorptive;
    }

    public Col getScale() {
        return scale;
    }

    public Vec getInc() {
        return inc;
    }

    public boolean isAbsorptive() {
        return absorptive;
    }
}

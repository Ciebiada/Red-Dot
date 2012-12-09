/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.math.OBasis;
import com.ciebiada.reddot.math.Vec;

public final class HitData {

    public double t, cosI;
    public Vec point;
    public Primitive primitive;
    public OBasis basis;

    public HitData() {
        t = Double.POSITIVE_INFINITY;
    }
}

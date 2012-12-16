/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.tracer;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Vec;

public class HitPoint {

    int x, y;
    Vec pos, nor;
    double r, g, b;

    public HitPoint(int x, int y, Vec pos, Vec nor, Col radiance) {
        this.x = x;
        this.y = y;

        this.pos = pos;
        this.nor = nor;

        r = radiance.r;
        g = radiance.g;
        b = radiance.b;
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

import com.ciebiada.reddot.math.Sample;

public class TriangleFilter implements Filter {

    @Override
    public Sample transform(Sample sample) {
        return new Sample(triangle(sample.getX()), triangle(sample.getY()));
    }

    private static double triangle(double r) {
        if (r < 0.5)
            return Math.sqrt(2 * r) - 1;
        else
            return 1 - Math.sqrt(2 * (1 - r));
    }
}

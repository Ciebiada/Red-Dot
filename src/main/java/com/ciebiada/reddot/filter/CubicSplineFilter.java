/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

import com.ciebiada.reddot.math.Sample;

public class CubicSplineFilter implements Filter {

    @Override
    public Sample transform(Sample sample) {
        return new Sample(cubicFilter(sample.getX()), cubicFilter(sample.getY()));
    }

    private static double cubicFilter(double x) {
        if (x < 1.0 / 24.0)
            return Math.pow(24 * x, 0.25f) - 2.0;
        else if (x < 0.5)
            return solve(24.0 * (x - 1.0 / 24.0) / 11.0) - 1.0;
        else if (x < 23.0 / 24.0)
            return 1.0 - solve(24.0 * (23.0 / 24.0 - x) / 11.0);
        else
            return 2 - Math.pow(24.0 * (1.0 - x), 0.25);
    }

    private static double solve(double r) {
        double u = r;
        for (int i = 0; i < 5; i++)
            u = (11.0 * r + u * u * (6.0 + u * (8.0 - 9.0 * u))) /
                    (4.0 + 12.0 * u * (1.0 + u * (1.0 - u)));
        return u;
    }
}

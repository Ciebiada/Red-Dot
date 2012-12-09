/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public final class Gaussian extends Filter {

    private final double alpha, exp;

    public Gaussian(double size, double alpha) {
        super(size);

        this.alpha = alpha;
        exp = Math.exp(-alpha * size * size);
    }

    @Override
    public double get(double x, double y) {
        double gx = Math.exp(-alpha * x * x) - exp;
        double gy = Math.exp(-alpha * y * y) - exp;
        return gx * gy;
    }
}

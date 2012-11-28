/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public class Gaussian extends Filter {

    private float alpha, exp;

    public Gaussian(float size, float alpha) {
        super(size);
        this.alpha = alpha;
        exp = (float) Math.exp(-alpha * size * size);
    }

    @Override
    public float get(float x, float y) {
        float gx = (float) Math.exp(-alpha * x * x) - exp;
        float gy = (float) Math.exp(-alpha * y * y) - exp;
        return gx * gy;
    }
}

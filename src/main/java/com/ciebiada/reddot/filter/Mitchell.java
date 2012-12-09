/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public class Mitchell extends Filter {

    private final double b, c, sizeInv;

    public Mitchell(double size, double b, double c) {
        super(size);
        this.b = b;
        this.c = c;
        sizeInv = 1 / size;
    }

    @Override
    public double get(double x, double y) {
        return mitchell1d(x * sizeInv) * mitchell1d(y * sizeInv);
    }

    private double mitchell1d(double x) {
        x = Math.abs(2 * x);
        if (x > 1)
            return ((-b - 6 * c) * x * x * x + (6 * b + 30 * c) * x * x +
                    (-12 * b - 48 * c) * x + (8 * b + 24 * c)) * (1.f / 6.f);
        else
            return ((12 - 9 * b - 6 * c) * x * x * x +
                    (-18 + 12 * b + 6 * c) * x * x +
                    (6 - 2 * b)) * (1.f / 6.f);
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public final class Tent extends Filter {

    public Tent(double size) {
        super(size);
    }

    @Override
    public double get(double x, double y) {
        return Math.max(0, getSize() - Math.abs(x)) * Math.max(0, getSize() - Math.abs(y));
    }
}

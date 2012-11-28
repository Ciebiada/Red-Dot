/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public class Tent extends Filter {

    public Tent(float size) {
        super(size);
    }

    @Override
    public float get(float x, float y) {
        return Math.max(0, getSize() - Math.abs(x)) * Math.max(0, getSize() - Math.abs(y));
    }
}

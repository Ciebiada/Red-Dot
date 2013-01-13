/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public abstract class Filter {

    protected float size;

    protected Filter(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public abstract float fun(float x, float y);
}

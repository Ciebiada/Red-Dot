package com.ciebiada.reddot.filter;

public class TentFilter extends Filter {

    public TentFilter(float size) {
        super(size);
    }

    @Override
    public float fun(float x, float y) {
        return Math.max(0, size - Math.abs(x)) * Math.max(0, size - Math.abs(y));
    }
}

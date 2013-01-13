package com.ciebiada.reddot.filter;

public class BoxFilter extends Filter {

    public BoxFilter(float size) {
        super(size);
    }

    @Override
    public float fun(float x, float y) {
        return 1;
    }
}

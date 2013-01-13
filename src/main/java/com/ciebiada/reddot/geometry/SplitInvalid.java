package com.ciebiada.reddot.geometry;

public class SplitInvalid extends Split {

    public SplitInvalid(float cost) {
        super(0, 0, 0, 0, 0, 0, cost);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}

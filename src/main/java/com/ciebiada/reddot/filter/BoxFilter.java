/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

import com.ciebiada.reddot.math.Sample;

public class BoxFilter implements Filter {

    @Override
    public Sample transform(Sample sample) {
        return new Sample(sample.getX() - 0.5, sample.getY() - 0.5);
    }
}

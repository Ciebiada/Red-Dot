/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.sampler;

import com.ciebiada.reddot.math.Sample;

import java.util.Random;

public class RandomSampler implements Sampler {

    private Random random;

    public RandomSampler(int seed) {
        random = new Random(seed);
    }

    @Override
    public float get1dSample() {
        return random.nextFloat();
    }

    @Override
    public Sample getSample() {
        return new Sample(get1dSample(), get1dSample());
    }

    @Override
    public void reset() {
    }
}
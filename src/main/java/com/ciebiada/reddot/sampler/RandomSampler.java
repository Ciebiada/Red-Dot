/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.sampler;

import java.util.Random;

public class RandomSampler extends Sampler {

    private final Random random;

    public RandomSampler(int seed) {
        random = new Random(seed);
    }

    @Override
    public double get1dSample() {
        return random.nextDouble();
    }

    @Override
    public double[] getSample() {
        return new double[] {get1dSample(), get1dSample()};
    }

    @Override
    public void finishThisPath() {
    }
}
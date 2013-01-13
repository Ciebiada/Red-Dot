/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.sampler;

import com.ciebiada.reddot.math.Sample;

public interface Sampler {

    float get1dSample();

    Sample getSample();

    void reset();
}
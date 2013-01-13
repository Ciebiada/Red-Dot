/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Sample;
import com.ciebiada.reddot.sampler.Sampler;

public interface Camera {

	Ray createRay(Sample sample, Sampler sampler);
}

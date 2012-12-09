/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.sampler;

public abstract class Sampler {

    public abstract double get1dSample();

    public abstract double[] getSample();

    public abstract void finishThisPath();
}
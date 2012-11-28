/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.sampler;

public abstract class Sampler {

    public abstract float get1dSample();

    public abstract float[] getSample();

    public abstract void finishThisPath();
}
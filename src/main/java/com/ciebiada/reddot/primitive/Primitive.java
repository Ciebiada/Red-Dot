/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Sample;
import com.ciebiada.reddot.math.Vec;

public abstract class Primitive {

    public abstract Vec[] sample(Sample sample);

    public abstract boolean hit(Ray ray, HitData hit);

    public abstract boolean shadowHit(Ray ray, double tmax);

    public abstract Material getMat();

    public abstract Vec getMin();

    public abstract Vec getMax();

    public abstract double getArea();

}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material;

public class AbsorptiveBrdf extends Brdf {

    public AbsorptiveBrdf() {
        super(null, null, false);
    }

    @Override
    public boolean isAbsorptive() {
        return true;
    }
}

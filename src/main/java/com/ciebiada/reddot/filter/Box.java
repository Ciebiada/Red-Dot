/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public final class Box extends Filter {
	
	public Box(double size) {
		super(size);
	}

    @Override
	public double get(double x, double y) {
		return 1;
	}
}

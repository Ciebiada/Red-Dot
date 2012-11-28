/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public class Box extends Filter {
	
	public Box(float size) {
		super(size);
	}

    @Override
	public float get(float x, float y) {
		return 1;
	}
}

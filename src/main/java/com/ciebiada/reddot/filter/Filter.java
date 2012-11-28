/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public abstract class Filter {

	private float size;
	
	public Filter(float size) {
		this.size = size;
	}
	
	public float getSize() {
		return size;
	}
	
	public abstract float get(float x, float y);
}

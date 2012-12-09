/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.filter;

public abstract class Filter {

	private final double size;
	
	public Filter(double size) {
		this.size = size;
	}
	
	public double getSize() {
		return size;
	}
	
	public abstract double get(double x, double y);
}

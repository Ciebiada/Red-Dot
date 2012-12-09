/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public final class Col {

    public final static Col BLACK = new Col(0);
    public final static Col WHITE = new Col(1);

	public final double r, g, b;

    public Col(double val) {
        r = g = b = val;
    }

	public Col(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Col add(Col c) {
		return new Col(r + c.r, g + c.g, b + c.b);
	}

	public Col sub(Col c) {
		return new Col(r - c.r, g - c.g, b - c.b);
	}
	
	public Col mul(double val) {
		return new Col(r * val, g * val, b * val);
	}
	
	public Col mul(Col c) {
		return new Col(r * c.r, g * c.g, b * c.b);
	}

	public Col div(double val) {
		val = 1 / val;
		return new Col(r * val, g * val, b * val);
	}

	public int toInt() {
		int r1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(r, 1.0f / 2.2f))));
		int g1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(g, 1.0f / 2.2f))));
		int b1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(b, 1.0f / 2.2f))));

		return (r1 << 16) + (g1 << 8) + b1;
	}

	public double lum() {
		return 0.2126 * r + 0.7152 * g + 0.0722 * b;
	}

    @Override
    public String toString() {
        return "(" + r + ", " + g + ", " + b + ")";
    }
}

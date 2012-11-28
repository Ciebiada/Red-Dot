/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public final class Col {

    public final static Col BLACK = new Col(0);
    public final static Col WHITE = new Col(1);

	public float r, g, b;

    public Col(float f) {
        r = g = b = f;
    }

	public Col(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Col(Col c) {
		this.r = c.r;
		this.g = c.g;
		this.b = c.b;
	}
	
	public Col add(Col c) {
		return new Col(r + c.r, g + c.g, b + c.b);
	}

	public Col sub(Col c) {
		return new Col(r - c.r, g - c.g, b - c.b);
	}
	
	public Col mul(float f) {
		return new Col(r * f, g * f, b * f);
	}
	
	public Col mul(Col c) {
		return new Col(r * c.r, g * c.g, b * c.b);
	}

	public Col div(float f) {
		f = 1 / f;
		return new Col(r * f, g * f, b * f);
	}

    public void addSet(Col c) {
        this.r += c.r;
        this.g += c.g;
        this.b += c.b;
    }

    public void subSet(Col c) {
        this.r -= c.r;
        this.g -= c.g;
        this.b -= c.b;
    }

    public void mulSet(Col c) {
        this.r *= c.r;
        this.g *= c.g;
        this.b *= c.b;
    }

    public void mulSet(float f) {
        r *= f;
        g *= f;
        b *= f;
    }

	public int toInt() {
		int r1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(r, 1.0f / 2.2f))));
		int g1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(g, 1.0f / 2.2f))));
		int b1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(b, 1.0f / 2.2f))));

		return (r1 << 16) + (g1 << 8) + b1;
	}

	public float lum() {
		return 0.27f * r + 0.67f * g + 0.06f * b;
	}

    public boolean isBlack() {
        return (r < 1e-5f && g < 1e-5f && b < 1e-5f);
    }

    @Override
    public String toString() {
        return "(" + r + ", " + g + ", " + b + ")";
    }

    public void set(Col c) {
        r = c.r;
        g = c.g;
        b = c.b;
    }

}

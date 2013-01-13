/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public class Col {

	public float r, g, b;

    public Col(float val) {
        r = g = b = val;
    }

	public Col(float r, float g, float b) {
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

    public Col mul(Col c) {
        return new Col(r * c.r, g * c.g, b * c.b);
    }

	public Col mul(float val) {
		return new Col(r * val, g * val, b * val);
	}

	public Col div(float val) {
        val = 1 / val;
		return new Col(r * val, g * val, b * val);
	}

    public void addSet(Col c) {
        r += c.r;
        g += c.g;
        b += c.b;
    }

    public void mulSet(Col c) {
        r *= c.r;
        g *= c.g;
        b *= c.b;
    }

    public void mulSet(float val) {
        r *= val;
        g *= val;
        b *= val;
    }

    public void divSet(float val) {
        val = 1 / val;
        r *= val;
        g *= val;
        b *= val;
    }

	public int toInt() {
		int r1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(r, 1.0f / 2.2f))));
		int g1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(g, 1.0f / 2.2f))));
		int b1 = Math.max(0, Math.min(255, (int) (255.0f * Math.pow(b, 1.0f / 2.2f))));

		return (r1 << 16) + (g1 << 8) + b1;
	}

	public float lum() {
		return 0.2126f * r + 0.7152f * g + 0.0722f * b;
	}

    @Override
    public String toString() {
        return "(" + r + ", " + g + ", " + b + ")";
    }
}

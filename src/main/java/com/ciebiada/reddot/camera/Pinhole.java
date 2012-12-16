/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.OBasis;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public final class Pinhole extends Camera {

	private final Vec eye;
	private final Vec corner;
	private final Vec toTop, toRight;
	
	public Pinhole(Vec eye, Vec gaze, double fov, double ratio) {
		this.eye = eye;

        double filmWidth = Math.tan(Math.toRadians(fov / 2)) * 2;
		double filmHeight = filmWidth * ratio;

        OBasis ob = new OBasis(gaze);

		corner = eye.add(gaze).sub(ob.getU().mul(filmWidth / 2)).sub(ob.getV().mul(filmHeight / 2));
		toTop = ob.getV().mul(filmHeight);
		toRight = ob.getU().mul(filmWidth);
	}
	
	public Ray getRay(double x, double y, Sampler sampler) {
		Vec onPlane = corner.add(toRight.mul(x)).add(toTop.mul(y));
		return new Ray(eye, onPlane.sub(eye).norm());
	}
}

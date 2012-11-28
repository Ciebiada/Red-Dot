/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public class Pinhole extends Camera {

	private Vec eye;
	private Vec corner;
	private Vec toTop, toRight;
	
	public Pinhole(Vec eye, Vec gaze, float fov, float ratio) {
		this.eye = eye;

        float filmWidth = (float) Math.tan(Math.toRadians(fov / 2)) * 2;
		float filmHeight = filmWidth * ratio;

        OrthoBasis ob = new OrthoBasis(gaze);

		corner = eye.add(gaze).sub(ob.getRight().mul(filmWidth / 2)).sub(ob.getUp().mul(filmHeight / 2));
		toTop = ob.getUp().mul(filmHeight);
		toRight = ob.getRight().mul(filmWidth);
	}
	
	public Vec getEye() {
		return eye;
	}
	
	public Ray getRay(float x, float y, Sampler sampler) {
		Vec onPlane = corner.add(toRight.mul(x)).add(toTop.mul(y));
		return new Ray(eye, onPlane.sub(eye).norm());
	}
}

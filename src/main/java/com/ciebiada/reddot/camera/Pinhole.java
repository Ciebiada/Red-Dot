/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.OBasis;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Sample;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public class Pinhole implements Camera {

	private Vec eye;
	private Vec corner;
	private Vec toTop, toRight;
	
	public Pinhole(Vec eye, Vec gaze, float fov, float ratio) {
		this.eye = eye;

        float filmWidth = (float) Math.tan(Math.toRadians(fov / 2)) * 2;
		float filmHeight = filmWidth * ratio;

        OBasis basis = new OBasis(gaze);

		corner = eye.add(gaze).sub(basis.u.mul(filmWidth / 2)).sub(basis.v.mul(filmHeight / 2));
		toTop = basis.v.mul(filmHeight);
		toRight = basis.u.mul(filmWidth);
	}

    @Override
	public Ray createRay(Sample sample, Sampler sampler) {
		Vec onPlane = corner.add(toRight.mul(sample.x)).add(toTop.mul(sample.y));
		return new Ray(eye, onPlane.sub(eye).norm());
	}
}

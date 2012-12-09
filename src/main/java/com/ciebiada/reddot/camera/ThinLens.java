/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.OBasis;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public final class ThinLens extends Camera {

	private final Vec eye;
	private final Vec corner;
	private final Vec toTop, toRight;

    private final OBasis ob;

    private final double lensSize;

	public ThinLens(Vec eye, Vec gaze, double fov, double ratio, double lensSize, double dist) {
		this.eye = eye;
        this.lensSize = lensSize;

		double filmWidth = Math.tan(Math.toRadians(fov / 2)) * dist * 2;
		double filmHeight = filmWidth * ratio;

        ob = new OBasis(gaze);

		corner = eye.add(gaze.mul(dist)).sub(ob.getU().mul(filmWidth / 2)).sub(ob.getV().mul(filmHeight / 2));
		toTop = ob.getV().mul(filmHeight);
		toRight = ob.getU().mul(filmWidth);
	}
	
	public Ray getRay(double x, double y, double[] sample) {
		Vec onPlane = corner.add(toRight.mul(x)).add(toTop.mul(y));
        double lx = lensSize * (sample[0] - 0.5f);
        double ly = lensSize * (sample[1] - 0.5f);
        Vec orig = eye.add(ob.getV().mul(ly)).add(ob.getU().mul(lx));

		return new Ray(orig, onPlane.sub(orig).norm());
	}
}

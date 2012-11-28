/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public class ThinLens extends Camera {

	private Vec eye;
	private Vec corner;
	private Vec toTop, toRight;

    private OrthoBasis ob;

    private float lensSize;

	public ThinLens(Vec eye, Vec gaze, float fov, float ratio, float lensSize, float dist) {
		this.eye = eye;
        this.lensSize = lensSize;

		float filmWidth = (float) Math.tan(Math.toRadians(fov / 2)) * dist * 2;
		float filmHeight = filmWidth * ratio;

        ob = new OrthoBasis(gaze);

		corner = eye.add(gaze.mul(dist)).sub(ob.getRight().mul(filmWidth / 2)).sub(ob.getUp().mul(filmHeight / 2));
		toTop = ob.getUp().mul(filmHeight);
		toRight = ob.getRight().mul(filmWidth);
	}
	
	public Vec getEye() {
		return eye;
	}
	
	public Ray getRay(float x, float y, Sampler sampler) {
		Vec onPlane = corner.add(toRight.mul(x)).add(toTop.mul(y));
        float lx = lensSize * (sampler.get1dSample() - 0.5f);
        float ly = lensSize * (sampler.get1dSample() - 0.5f);
        Vec orig = eye.add(ob.getUp().mul(ly)).add(ob.getRight().mul(lx));

		return new Ray(orig, onPlane.sub(orig).norm());
	}
}

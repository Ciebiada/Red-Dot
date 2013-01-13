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

public class ThinLens implements Camera {

	private Vec eye;
	private Vec corner;
	private Vec toTop, toRight;

    private OBasis basis;

    private float lensSize;

	public ThinLens(Vec eye, Vec gaze, float fov, float ratio, float lensSize, float dist) {
		this.eye = eye;
        this.lensSize = lensSize;

		float filmWidth = (float) Math.tan(Math.toRadians(fov / 2)) * dist * 2;
		float filmHeight = filmWidth * ratio;

        basis = new OBasis(gaze);

		corner = eye.add(gaze.mul(dist)).sub(basis.u.mul(filmWidth / 2)).sub(basis.v.mul(filmHeight / 2));
		toTop = basis.v.mul(filmHeight);
		toRight = basis.u.mul(filmWidth);
	}

    @Override
	public Ray createRay(Sample sample, Sampler sampler) {
        Sample lensSample = sampler.getSample();

		Vec onPlane = corner.add(toRight.mul(sample.x)).add(toTop.mul(sample.y));
        float lx = lensSize * (lensSample.x - 0.5f);
        float ly = lensSize * (lensSample.y - 0.5f);
        Vec orig = eye.add(basis.v.mul(ly)).add(basis.u.mul(lx));

		return new Ray(orig, onPlane.sub(orig).norm());
	}
}

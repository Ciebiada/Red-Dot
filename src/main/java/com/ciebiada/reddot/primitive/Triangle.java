/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Utils;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public abstract class Triangle extends Primitive {

	protected Mesh mesh;

	public Triangle(Mesh mesh) {
		this.mesh = mesh;
	}

    public abstract Vec getP0();

    public abstract Vec getP1();

    public abstract Vec getP2();

    public abstract Vec getNormal(float beta, float gamma);

    @Override
    public Material getMat() {
		return mesh.mat;
	}

    @Override
	public Vec[] sample(float[] sample) {
		float s = (float) Math.sqrt(sample[0]);
		float t = sample[1];

		Vec point =  getP0().mul(1 - s).add(getP1().mul(s * (1 - t))).add(getP2().mul(s * t));
        return new Vec[] {point, getNormal(s, t)};
	}

    @Override
	public Vec getMin() {
		return new Vec(
                Utils.min(getP0().x, getP1().x, getP2().x),
                Utils.min(getP0().y, getP1().y, getP2().y),
                Utils.min(getP0().z, getP1().z, getP2().z)
        );
	}

    @Override
	public Vec getMax() {
        return new Vec(
                Utils.max(getP0().x, getP1().x, getP2().x),
                Utils.max(getP0().y, getP1().y, getP2().y),
                Utils.max(getP0().z, getP1().z, getP2().z)
        );
	}

    @Override
	public abstract float getArea();

    @Override
	public boolean hit(Ray ray) {
        float e1x = getP1().x - getP0().x;
        float e1y = getP1().y - getP0().y;
        float e1z = getP1().z - getP0().z;

        float e2x = getP2().x - getP0().x;
        float e2y = getP2().y - getP0().y;
        float e2z = getP2().z - getP0().z;

        float pvecx = ray.dir.y * e2z - ray.dir.z * e2y;
        float pvecy = ray.dir.z * e2x - ray.dir.x * e2z;
        float pvecz = ray.dir.x * e2y - ray.dir.y * e2x;

        float det = e1x * pvecx + e1y * pvecy + e1z * pvecz;

        if (det < Utils.EPS)
            return false;

        float tvecx = ray.orig.x - getP0().x;
        float tvecy = ray.orig.y - getP0().y;
        float tvecz = ray.orig.z - getP0().z;

        float u = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
        if (u < 0.0f || u > det)
            return false;

        float qvecx = tvecy * e1z - tvecz * e1y;
        float qvecy = tvecz * e1x - tvecx * e1z;
        float qvecz = tvecx * e1y - tvecy * e1x;

        float v = ray.dir.x * qvecx + ray.dir.y * qvecy + ray.dir.z * qvecz;
        if (v < 0.0f || u + v > det)
            return false;

        float t = e2x * qvecx + e2y * qvecy + e2z * qvecz;
        float detInv = 1 / det;
        t *= detInv;
        u *= detInv;
        v *= detInv;

        if (t > Utils.EPS && t < ray.t) {
	    	ray.t = t;
	    	ray.hit = this;
	    	ray.ip = ray.orig.add(ray.dir.mul(t));
            ray.nor = getNormal(u, v);
            ray.cosI = -ray.nor.dot(ray.dir);
	        return true;
	    }

        return false;
	}

    @Override
	public boolean shadowHit(Ray ray) {
        float e1x = getP1().x - getP0().x;
        float e1y = getP1().y - getP0().y;
        float e1z = getP1().z - getP0().z;

        float e2x = getP2().x - getP0().x;
        float e2y = getP2().y - getP0().y;
        float e2z = getP2().z - getP0().z;

        float pvecx = ray.dir.y * e2z - ray.dir.z * e2y;
        float pvecy = ray.dir.z * e2x - ray.dir.x * e2z;
        float pvecz = ray.dir.x * e2y - ray.dir.y * e2x;

        float det = e1x * pvecx + e1y * pvecy + e1z * pvecz;

        if (det < Utils.EPS)
            return false;

        float tvecx = ray.orig.x - getP0().x;
        float tvecy = ray.orig.y - getP0().y;
        float tvecz = ray.orig.z - getP0().z;

        float u = tvecx * pvecx + tvecy * pvecy + tvecz * pvecz;
        if (u < 0.0f || u > det)
            return false;

        float qvecx = tvecy * e1z - tvecz * e1y;
        float qvecy = tvecz * e1x - tvecx * e1z;
        float qvecz = tvecx * e1y - tvecy * e1x;

        float v = ray.dir.x * qvecx + ray.dir.y * qvecy + ray.dir.z * qvecz;
        if (v < 0.0f || u + v > det)
            return false;

        float t = e2x * qvecx + e2y * qvecy + e2z * qvecz;
        float detInv = 1 / det;
        t *= detInv;
        u *= detInv;
        v *= detInv;

        return (t > Utils.EPS && t < ray.t);
	}
}

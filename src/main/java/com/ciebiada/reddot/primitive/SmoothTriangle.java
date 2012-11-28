/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;

public class SmoothTriangle extends Triangle {

	private int a, b, c;

    private float area;

	public SmoothTriangle(int a, int b, int c, Mesh mesh) {
        super(mesh);

        this.a = a;
        this.b = b;
        this.c = c;

        area = getP0().cross(getP1()).add(getP1().cross(getP2())).add(getP2().cross(getP0())).length() / 2;
    }

    @Override
    public Vec getP0() {
        return mesh.verts[a];
    }

    @Override
    public Vec getP1() {
        return mesh.verts[b];
    }

    @Override
    public Vec getP2() {
        return mesh.verts[c];
    }

    public Vec getN0() {
        return mesh.normals[a];
    }

    public Vec getN1() {
        return mesh.normals[b];
    }

    public Vec getN2() {
        return mesh.normals[c];
    }

    @Override
    public Vec getNormal(float beta, float gamma) {
        Vec smoothNormal = getN0().add(getN1().sub(getN0()).mul(beta)).add(getN2().sub(getN0()).mul(gamma));
        return smoothNormal;
    }

    @Override
    public float getArea() {
        return area;
    }
}

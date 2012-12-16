/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.math.Vec;

public final class SmoothTriangle extends Triangle {

    private final int a, b, c;
    private final Vec nor;
    private final double area;

    public SmoothTriangle(int a, int b, int c, Mesh mesh) {
        super(mesh);

        this.a = a;
        this.b = b;
        this.c = c;

        nor = getP1().sub(getP0()).cross(getP2().sub(getP0())).norm();

        area = getP0().cross(getP1()).add(getP1().cross(getP2())).add(getP2().cross(getP0())).length() / 2;
    }

    @Override
    public Vec getP2() {
        return mesh.verts[c];
    }

    @Override
    public Vec getP1() {
        return mesh.verts[b];
    }

    @Override
    public Vec getP0() {
        return mesh.verts[a];
    }

    @Override
    public Vec getNormal(double beta, double gamma) {
        return getN0().add(getN1().sub(getN0()).mul(beta)).add(getN2().sub(getN0()).mul(gamma));
    }

    @Override
    public Vec getGeometricNormal() {
        return nor;
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
    public double getArea() {
        return area;
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.math.Vec;

public final class SolidTriangle extends Triangle {

    private final Vec p0, p1, p2;
    private final Vec nor;
    private final double area;

    public SolidTriangle(int a, int b, int c, Mesh mesh) {
        super(mesh);

        p0 = mesh.verts[a];
        p1 = mesh.verts[b];
        p2 = mesh.verts[c];

        nor = p1.sub(p0).cross(p2.sub(p0)).norm();

        area = getP0().cross(getP1()).add(getP1().cross(getP2())).add(getP2().cross(getP0())).length() / 2;
    }

    @Override
    public Vec getP2() {
        return p2;
    }

    @Override
    public Vec getP1() {
        return p1;
    }

    @Override
    public Vec getP0() {
        return p0;
    }

    @Override
    public Vec getNormal(double beta, double gamma) {
        return nor;
    }

    @Override
    public Vec getGeometricNormal() {
        return nor;
    }

    @Override
    public double getArea() {
        return area;
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public final class Vec {

    public final double x, y, z;

    public Vec(double val) {
        x = y = z = val;
    }

    public Vec(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec(Vec v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vec add(Vec v) {
        return new Vec(x + v.x, y + v.y, z + v.z);
    }

    public Vec sub(Vec v) {
        return new Vec(x - v.x, y - v.y, z - v.z);
    }

    public Vec mul(Vec v) {
        return new Vec(x * v.x, y * v.y, z * v.z);
    }

    public Vec mul(double val) {
        return new Vec(x * val, y * val, z * val);
    }

    public Vec div(Vec val) {
        return new Vec(x / val.x, y / val.y, z / val.z);
    }

    public Vec div(double val) {
        val = 1.0f / val;
        return new Vec(x * val, y * val, z * val);
    }

    public double dot(Vec v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec cross(Vec v) {
        return new Vec(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public double get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                return z;
        }
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec norm() {
        double f = 1 / Math.sqrt(x * x + y * y + z * z);
        return new Vec(x * f, y * f, z * f);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

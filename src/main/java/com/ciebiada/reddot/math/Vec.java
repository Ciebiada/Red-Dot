/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public final class Vec {

    public final static Vec INVALID = new Vec(0);

    public float x, y, z;

    public Vec(float f) {
        x = y = z = f;
    }

    public Vec(float x, float y, float z) {
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

    public Vec mul(float f) {
        return new Vec(x * f, y * f, z * f);
    }

    public Vec div(Vec v) {
        return new Vec(x / v.x, y / v.y, z / v.z);
    }

    public void addSet(Vec v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    public void subSet(Vec v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    public void mulSet(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
    }

    public Vec div(float f) {
        f = 1.0f / f;
        return new Vec(x * f, y * f, z * f);
    }

    public float dot(Vec v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec cross(Vec v) {
        return new Vec(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                return z;
        }
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vec norm() {
        float f = 1 / (float) Math.sqrt(x * x + y * y + z * z);
        return new Vec(x * f, y * f, z * f);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

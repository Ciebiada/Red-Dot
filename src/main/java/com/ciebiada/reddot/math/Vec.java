/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public class Vec {

    public float x, y, z;

    public Vec(float val) {
        x = y = z = val;
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

    public void addSet(Vec v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public Vec sub(Vec v) {
        return new Vec(x - v.x, y - v.y, z - v.z);
    }

    public void subSet(Vec v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public Vec mul(Vec v) {
        return new Vec(x * v.x, y * v.y, z * v.z);
    }

    public Vec mul(float val) {
        return new Vec(x * val, y * val, z * val);
    }

    public Vec div(Vec val) {
        return new Vec(x / val.x, y / val.y, z / val.z);
    }

    public Vec div(float val) {
        val = 1 / val;
        return new Vec(x * val, y * val, z * val);
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

    public float get(int axis) {
        switch (axis) {
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
        float lengthInv = 1 / (float) Math.sqrt(x * x + y * y + z * z);
        return new Vec(x * lengthInv, y * lengthInv, z * lengthInv);
    }

    public void set(int axis, float val) {
        switch (axis) {
            case 0:
                x = val;
                break;
            case 1:
                y = val;
                break;
            default:
                z = val;
        }
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

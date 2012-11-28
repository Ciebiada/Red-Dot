/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public class Utils {

    public static final float PI = (float) Math.PI;

    public static final float EPS = 1e-5f;

    public static float min(float a, float b) {
        if (b < a)
            a = b;

        return a;
    }

    public static float max(float a, float b) {
        if (b > a)
            a = b;

        return a;
    }

    public static int min(int a, int b) {
        if (b < a)
            a = b;

        return a;
    }

    public static int max(int a, int b) {
        if (b > a)
            a = b;

        return a;
    }

    public static float max(Col c) {
        float ret = c.r;

        if (c.g > ret)
            ret = c.g;
        if (c.b > ret)
            ret = c.b;

        return ret;
    }

    public static float max(Vec v) {
        float ret = v.x;

        if (v.y > ret)
            ret = v.y;
        if (v.z > ret)
            ret = v.z;

        return ret;
    }

    public static float min(Vec v) {
        float ret = v.x;

        if (v.y < ret)
            ret = v.y;
        if (v.z < ret)
            ret = v.z;

        return ret;
    }

    public static float min(float a, float b, float c) {
        if (b < a)
            a = b;
        if (c < a)
            a = c;

        return a;
    }

    public static float max(float a, float b, float c) {
        if (b > a)
            a = b;
        if (c > a)
            a = c;

        return a;
    }

    public static int simpleCeil(float v) {
        v += 1 - EPS;
        return (int) v;
    }

    public static int simpleFloor(float v) {
        return (int) v;
    }

    public static float abs(float v) {
        return (v < 0) ? -v : v;
    }

    public static float pow(float base, float exp) {
        return (float) Math.pow(base, exp) + EPS;
    }

    public static float cantReachOne(float v) {
        return min(v, 1 - EPS);
    }
}

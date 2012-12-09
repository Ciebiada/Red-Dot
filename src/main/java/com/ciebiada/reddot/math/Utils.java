/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.math;

public class Utils {

    public static final double EPS = 1e-7;


    public static double min(double a, double b, double c) {
        if (b < a)
            a = b;
        if (c < a)
            a = c;

        return a;
    }

    public static double max(double a, double b, double c) {
        if (b > a)
            a = b;
        if (c > a)
            a = c;

        return a;
    }

    public static int simpleCeil(double v) {
        v += 1 - EPS;
        return (int) v;
    }

    public static int simpleFloor(double v) {
        return (int) v;
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.camera;

import com.ciebiada.reddot.math.Utils;
import com.ciebiada.reddot.math.Vec;

public class OrthoBasis {

    private Vec right, up, forward;

    public OrthoBasis(Vec forward) {
        this.forward = forward;

        if (Utils.abs(forward.x) > Utils.abs(forward.y)) {
            up = new Vec(-forward.z, 0, forward.x).norm();
        } else {
            up = new Vec(0, forward.z, -forward.y).norm();
        }

        right = forward.cross(up);
    }

    public Vec getUp() {
        return up;
    }

    public Vec getRight() {
        return right;
    }

    public Vec getForward() {
        return forward;
    }

    public Vec transform(Vec v) {
        return right.mul(v.x).add(up.mul(v.y)).add(forward.mul(v.z));
    }

}

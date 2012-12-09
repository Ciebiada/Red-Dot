/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Vec;

public class Mesh {

    protected final Vec[] verts;
    protected final Vec[] normals;
    protected final Material mat;

    public Mesh(Vec[] verts, Vec[] normals, Material mat) {
        this.verts = verts;
        this.normals = normals;
        this.mat = mat;
    }
}

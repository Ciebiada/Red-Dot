/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.primitive;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Vec;

public class Mesh {

	protected Vec[] verts;
    protected Vec[] normals;

    protected Material mat;
	
	public Mesh(Vec[] verts, Vec[] normals, Material mat) {
		this.verts = verts;
        this.normals = normals;
		this.mat = mat;
	}
}

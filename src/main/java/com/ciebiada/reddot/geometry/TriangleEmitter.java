package com.ciebiada.reddot.geometry;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Sample;
import com.ciebiada.reddot.math.Vec;

public class TriangleEmitter extends Triangle {

    private Vec a, b, c;
    private float area;

    public TriangleEmitter(Vec a, Vec b, Vec c, Material material) {
        super(a, b, c, material);

        this.a = a;
        this.b = b;
        this.c = c;

        area = a.cross(b).add(b.cross(c)).add(c.cross(a)).length() / 2;
    }

    public float getArea() {
        return area;
    }

    public Vec[] sample(Sample sample) {
        float s = (float) Math.sqrt(sample.x);
        float t = sample.y;

        Vec point = a.mul(1 - s).add(b.mul(s * (1 - t))).add(c.mul(s * t));

        return new Vec[] {point, getNor()};
    }
}

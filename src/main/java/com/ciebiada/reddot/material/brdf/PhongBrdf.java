/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.material.brdf;

import com.ciebiada.reddot.camera.OrthoBasis;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Utils;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.Sampler;

public class PhongBrdf extends Brdf {

    private Col specular;
    private int n;

    public PhongBrdf(Col specular, int n) {
        this.specular = specular;
        this.n = n;
    }

    @Override
    public Vec sampleDirection(Ray ray, float[] sample) {
        float phi = 2 * Utils.PI * sample[0];
        float cosT = (float) Math.pow(sample[1], 1.0f / (n + 1));
        float sinT = (float) Math.sqrt(1 - cosT * cosT);

        Vec dir = new Vec(
                sinT * (float) Math.cos(phi),
                sinT * (float) Math.sin(phi),
                cosT
        );

        Vec perfect = ray.dir.add(ray.nor.mul(2.0f * ray.cosI));

//        return perfect;
        return new OrthoBasis(perfect).transform(dir);
    }


    public static final float fastPow(float a, float b) {
        // adapted from: http://www.dctsystems.co.uk/Software/power.html
        float x = Float.floatToRawIntBits(a);
        x *= 1.0f / (1 << 23);
        x = x - 127;
        float y = x - (int) Math.floor(x);
        b *= x + (y - y * y) * 0.346607f;
        y = b - (int) Math.floor(b);
        y = (y - y * y) * 0.33971f;
        return Float.intBitsToFloat((int) ((b + 127 - y) * (1 << 23)));
    }

    @Override
    public Col evaluate(Ray ray, Vec out) {
        Vec perfect = ray.dir.add(ray.nor.mul(2.0f * ray.cosI));

        float cosT = Utils.abs(perfect.dot(out));

        float pow = fastPow(cosT, n);//float) Math.pow(cosT, n);

        if (pow < 0.00001f) pow = 0.00001f;
//        pow = 1;
//        return specular.mul((n + 2) / (n + 1));
        return specular.mul((n + 2) * pow / (2 * Utils.PI));
    }

    @Override
    public float getPdf(Ray ray, Vec out) {
        Vec perfect = ray.dir.add(ray.nor.mul(2.0f * ray.cosI));

        float cosT = Utils.abs(perfect.dot(out));

        float pow = fastPow(cosT, n);//float) Math.pow(cosT, n);

        if (pow < 0.00001f) pow = 0.00001f;


        return (n + 1) * pow / (2 * Utils.PI);
//        Vec wh = ray.dir.mul(-1).add(out).norm();
//        float cosT = Utils.abs(out.dot(wh));
//        float pdf = (n + 1) * Utils.pow(cosT, n) / (2 * Utils.PI * 4 * ray.dir.mul(-1).dot(wh));
//        if (ray.dir.mul(-1).dot(wh) <= 0) pdf = 0;
//        return pdf;
    }

    @Override
    public boolean isSpecular() {
        return true;
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;


import com.ciebiada.reddot.material.Brdf;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.primitive.HitData;
import com.ciebiada.reddot.sampler.HaltonSampler;
import com.ciebiada.reddot.sampler.Sampler;

public final class Trace extends Thread {

    private Scene scene;
    private Sampler sampler;

    public Trace(Scene scene, int seed) {
        this.scene = scene;
        sampler = new HaltonSampler(seed);
    }

    public void run() {
        while (true) {
            double[] sample = sampler.getSample();
            Col pixelValue = traceRay(scene.camera.getRay(sample[0], sample[1], sampler.getSample()));
            scene.film.store(sample[0], sample[1], pixelValue);
            sampler.finishThisPath();
        }
    }

    private Col traceRay(Ray ray) {
        Col w = new Col(1);
        Col l = new Col(0);

        for (int depth = 0; depth < 10; depth++) {
            HitData hit = new HitData();
            if (!scene.bvh.hit(ray, hit))
                break;

            l = l.add(w.mul(hit.primitive.getMat().getEmittance()));

            if (depth > 2) {
                double r = sampler.get1dSample();
                double sp = Math.max(0.1, w.lum());
                if (r > sp) {
                    break;
                } else {
                    w = w.div(sp);
                }
            }

            Brdf brdf = hit.primitive.getMat().getBrdf(ray, hit, sampler);

            if (brdf.isAbsorptive())
                break;

            w = w.mul(brdf.getScale());

            ray = new Ray(hit.point, brdf.getInc());
        }

        return l;
    }
}

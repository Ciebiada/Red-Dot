/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;


import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.material.brdf.Brdf;
import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.primitive.Primitive;
import com.ciebiada.reddot.sampler.*;

public final class Trace extends Thread {

    private Scene scene;
    private Sampler sampler;

    public Trace(Scene scene, int seed) {
        this.scene = scene;
        sampler = new HaltonSampler(seed);
    }

    public void run() {
        while (true) {
//        while (scene.film.getSamples() < 32 * scene.film.getHeight() * scene.film.getWidth()) {
            float[] sample = sampler.getSample();
            Col pixelValue = traceRay(scene.camera.getRay(sample[0], sample[1], sampler));
            scene.film.store(sample[0], sample[1], pixelValue);
            sampler.finishThisPath();
        }
    }

    private Col traceRay(Ray ray) {
        if (!scene.bvh.hit(ray))
            return Col.BLACK;

        Col collected = new Col(0);
        Col weight = new Col(1);

        collected.addSet(ray.hit.getMat().getEmittance());

        for (int depth = 0; depth < 10; ++depth) {
            Material mat = ray.hit.getMat();

            Brdf brdf = mat.getBrdf(ray, sampler);

            Vec bounce = brdf.sampleDirection(ray, sampler.getSample());

            if (bounce == Vec.INVALID)
                break;

            float brdfPdf = brdf.getPdf(ray, bounce);

            collected.addSet(weight.mul(sampleLights(ray, brdf)));

            weight.mulSet(brdf.evaluate(ray, bounce).mul(ray.cosI / brdfPdf));

            ray.setRay(ray.ip, bounce);

            if (!scene.bvh.hit(ray))
                break;

            collected.addSet(weight.mul(sampleBrdf(ray, brdf, brdfPdf)));

            if (depth > 3) {
                float sp = Utils.max(0.1f, Utils.max(weight));
                if (sampler.get1dSample() > sp)
                    break;
                weight.mulSet(1 / sp);
            }
        }

        return collected;
    }

    private Col sampleBrdf(Ray ray, Brdf brdf, float brdfPdf) {
        if (ray.hit.getMat().isEmissive()) {
            float lightPdf = 1 / ray.hit.getArea();
//            float weight = 1;//powerHeuristic(brdfPdf, lightPdf);;
//            if (!brdf.isSpecular())
//                weight = 0;
//                float weight = powerHeuristic(brdfPdf, lightPdf);
            float weight = balanceHeuristic(brdfPdf, lightPdf);
            return ray.hit.getMat().getEmittance().mul(weight);
        }

        return Col.BLACK;
    }

    private Col sampleLights(Ray ray, Brdf brdf) {
        float[] weights = new float[scene.lights.size()];

        float[] sample = sampler.getSample();

        float weightsSum = 0;
        for (int i = 0; i < weights.length; i++) {
//            float w = 1.0f / scene.lights.size();
            float w = estimateLight(scene.lights.get(i), sample, ray);
            weights[i] = w;
            weightsSum += w;
        }

        if (weightsSum <= Utils.EPS)
            return Col.BLACK;

        for (int i = 0; i < weights.length; i++) {
            weights[i] /= weightsSum;
        }

        int pick = -1;
        float p = 0;
        float sample1d = sampler.get1dSample();
        for (int i = 0; i < weights.length; i++) {
            if (sample1d < p + weights[i]) {
                pick = i;
                break;
            }
            p += weights[i];
        }

        if (pick == -1)
            return Col.BLACK;

        Primitive light = scene.lights.get(pick);
//        sample[0] = (sample[0] - p) / weights[pick];

        float pdf = weights[pick];
        return sampleLight(light, sample, ray, brdf, pdf);
    }

    private Col sampleLight(Primitive light, float[] sample, Ray ray, Brdf brdf, float pdf) {
        Vec[] lightSample = light.sample(sample);
        Vec toLight = lightSample[0].sub(ray.ip);
        float dist2 = toLight.dot(toLight);
        float dist = (float) Math.sqrt(dist2);
        toLight = toLight.div(dist);
        Ray shadowRay = new Ray(ray.ip, toLight);
        shadowRay.t = dist - Utils.EPS;

        if (scene.bvh.shadowHit(shadowRay))
            return Col.BLACK;

        float cos1 = toLight.dot(ray.nor);
        float cos2 = -toLight.dot(lightSample[1]);

        if (cos1 > 0.0f && cos2 > 0.0f) {
            float g = cos1 * cos2 / dist2;
            Col rho = brdf.evaluate(ray, toLight);
            float brdfPdf = brdf.getPdf(ray, toLight);
            float lightPdf = 1 / light.getArea();
//            float weight = 0;
//            if (!brdf.isSpecular())
//                weight = 1;
//                float weight =powerHeuristic(lightPdf, brdfPdf);
//            float weight = 0.8f;
            float weight = balanceHeuristic(lightPdf, brdfPdf);
            return rho.mul(light.getMat().getEmittance()).mul(weight * g / lightPdf / pdf);
        } else {
            return Col.BLACK;
        }
    }

    private float estimateLight(Primitive light, float[] sample, Ray ray) {
        Vec[] lightSample = light.sample(sample);
        Vec toLight = lightSample[0].sub(ray.ip);
        float dist2 = toLight.dot(toLight);
        float dist = (float) Math.sqrt(dist2);
        toLight = toLight.div(dist);

        float cos1 = toLight.dot(ray.nor);
        float cos2 = -toLight.dot(lightSample[1]);

        if (cos1 > 0.0f && cos2 > 0.0f) {
            float g = cos1 * cos2 / dist2;
            float lightPdf = 1 / light.getArea();

            return g / lightPdf * scene.lights.size();
        } else {
            return 0;
        }
    }

    private float balanceHeuristic(float pdf1, float pdf2) {
        return pdf1 / (pdf1 + pdf2);
    }

    private float powerHeuristic(float pdfA, float pdfB) {
//        return pdfA / (pdfA + pdfB);
        float pdfA2 = pdfA * pdfA;
        return (pdfA2) / (pdfA2 + pdfB * pdfB);
    }
}

/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.tracer;

import com.ciebiada.reddot.Scene;
import com.ciebiada.reddot.geometry.StackElem;
import com.ciebiada.reddot.geometry.TriangleRaw;
import com.ciebiada.reddot.material.Brdf;
import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Sample;
import com.ciebiada.reddot.math.Vec;
import com.ciebiada.reddot.sampler.HaltonSampler;
import com.ciebiada.reddot.sampler.Sampler;

import java.util.Random;

public class Tracer extends Thread {

    private Scene scene;
    private int pixelIter;
    private int threadCount;

    public Tracer(Scene scene, int threadCount) {
        this.scene = scene;
        this.threadCount = threadCount;
    }

    @Override
    public void run() {
        Sampler[] samplers = new Sampler[threadCount];
        Random random = new Random();
        for (int i = 0; i < threadCount; i++) {
            samplers[i] = new HaltonSampler(random.nextInt());
        }

        Thread[] threads = new Thread[threadCount];

        int passes = 0;

        long start = System.currentTimeMillis();

        for (;;) {
            for (int i = 0; i < threadCount; i++) {
                threads[i] = new RenderPixel(samplers[i], scene.film.pixelCount);
                threads[i].start();
            }

            for (Thread thread: threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                }
            }

            pixelIter = 0;

            passes++;

            if (passes == 32) {
                System.out.println(passes + " passes took: "
                        + (System.currentTimeMillis() - start) / 1000.0 + " sec");
            }
        }
    }

    private class RenderPixel extends Thread {

        private Sampler sampler;
        private int pixelCount;
        private StackElem[] stack;

        public RenderPixel(Sampler sampler, int pixelCount) {
            this.sampler = sampler;
            this.pixelCount = pixelCount;

            stack = new StackElem[30];
            for (int i = 0; i < stack.length; i++)
                stack[i] = new StackElem();
        }

        @Override
        public void run() {
            for (;;) {
                if (pixelIter >= pixelCount)
                    break;
                pixelIter++;

                Sample cameraSample = sampler.getSample();

                Ray ray = scene.camera.createRay(cameraSample, sampler);

                scene.film.store(cameraSample, traceRay(ray));

                sampler.reset();
            }
        }

        private Col traceRay(Ray ray) {
            Col radiance = new Col(0);
            Col weight = new Col(1);

            for (int depth = 0; depth < 10; depth++) {
                if (depth > 2) {
                    float sp = Math.max(weight.r, Math.max(weight.g, weight.b));
                    if (sampler.get1dSample() > sp || sp == 0)
                        break;
                    weight.divSet(sp);
                }

                if (!scene.kdtree.rayIntersection(ray, stack))
                    break;

                Material mat = ray.tri.getMat();

                if (depth == 0)
                    radiance.addSet(mat.getEmittance().mul(weight));

                Brdf brdf = mat.getBrdf(ray, sampler);

                if (brdf.isAbsorptive())
                    break;

                weight.mulSet(brdf.scale);

                Vec ip = ray.orig.add(ray.dir.mul(ray.tmax)).add(ray.nor.mul(1e-5f));

                radiance.addSet(weight.mul(sampleLights(ray, ip)));

                ray.set(ip, brdf.dir);
            }

            return radiance;
        }

        private Col sampleLights(Ray ray, Vec ip) {
            float[] weights = new float[scene.lights.length];

            Sample sample = sampler.getSample();

            float weightsSum = 0;
            for (int i = 0; i < weights.length; i++) {
                float w = estimateLight(scene.lights[i], sample, ray, ip);
                weights[i] = w;
                weightsSum += w;
            }

            if (weightsSum <= 1e-5f)
                return new Col(0);

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
                return new Col(0);

            TriangleRaw light = scene.lights[pick];

            float pdf = weights[pick];
            return sampleLight(light, sample, pdf, ray, ip);
        }

        private Col sampleLight(TriangleRaw light, Sample sample, float pdf, Ray ray, Vec ip) {
            Vec[] lightSample = light.sample(sample);
            Vec toLight = lightSample[0].sub(ip);
            float dist2 = toLight.dot(toLight);
            float dist = (float) Math.sqrt(dist2);
            toLight = toLight.div(dist);
            Ray shadowRay = new Ray(ip, toLight);
            shadowRay.tmax = dist - 1e-5f;

            if (scene.kdtree.shadowRayIntersection(shadowRay, stack))
                return new Col(0);

            float cos1 = toLight.dot(ray.nor);
            float cos2 = -toLight.dot(lightSample[1]);

            if (cos1 > 0.0f && cos2 > 0.0f) {
                float g = cos1 * cos2 / dist2;
                float lightPdf = 1 / light.getArea();
                return light.getMat().getEmittance().mul(g / lightPdf / pdf / (float) Math.PI);
            } else {
                return new Col(0);
            }
        }

        private float estimateLight(TriangleRaw light, Sample sample, Ray ray, Vec ip) {
            Vec[] lightSample = light.sample(sample);
            Vec toLight = lightSample[0].sub(ip);
            float dist2 = toLight.dot(toLight);
            float dist = (float) Math.sqrt(dist2);
            toLight = toLight.div(dist);

            float cos1 = toLight.dot(ray.nor);
            float cos2 = -toLight.dot(lightSample[1]);

            if (cos1 > 0.0f && cos2 > 0.0f) {
                float g = cos1 * cos2 / dist2;
                float lightPdf = 1 / light.getArea();

                return g / lightPdf * scene.lights.length;
            } else {
                return 0;
            }
        }

    }
}

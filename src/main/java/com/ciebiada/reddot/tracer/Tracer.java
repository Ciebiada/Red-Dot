/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.tracer;


import com.ciebiada.reddot.Scene;
import com.ciebiada.reddot.material.Brdf;
import com.ciebiada.reddot.material.Diffuse;
import com.ciebiada.reddot.math.*;
import com.ciebiada.reddot.primitive.HitData;
import com.ciebiada.reddot.primitive.Primitive;
import com.ciebiada.reddot.sampler.QMCSampler;
import com.ciebiada.reddot.sampler.RandomSampler;
import com.ciebiada.reddot.sampler.Sampler;

import java.util.Random;

public final class Tracer extends Thread {

    private Scene scene;
    private Sampler pixelSampler, pathSampler, photonSampler;
    private Random random;
    private int[][] pixels;

    public Tracer(Scene scene, int seed) {
        this.scene = scene;

        pixelSampler = new QMCSampler(seed);
        pathSampler = new QMCSampler(seed);
        photonSampler = new QMCSampler(seed);

        random = new Random(seed);

    }

    private class HitPointsWorker extends Thread {
        int from, to;
        Sample pixelSample;
        Sampler sampler;
        HitMap hitMap;

        private HitPointsWorker(int from, int to, Sample pixelSample, Sampler sampler, HitMap hitMap) {
            this.from = from;
            this.to = to;
            this.pixelSample = pixelSample;
            this.sampler = sampler;
            this.hitMap = hitMap;
        }

        @Override
        public void run() {
            for (int i = from; i < to; i++) {
                double fx = (double) (pixels[i][0] + 0.5 + pixelSample.getX()) / scene.film.getWidth();
                double fy = (double) (pixels[i][1] + 0.5 + pixelSample.getY()) / scene.film.getHeight();
//                pixels[i][0] = (int) (pixels[i][0] + 0.5 + pixelSample[0]);
//                pixels[i][1] = (int) (pixels[i][1] + 0.5 + pixelSample[1]);
                Ray ray = scene.camera.getRay(fx, fy, pathSampler);
                traceRay(ray, hitMap, pixels[i][0], pixels[i][1], sampler);
                sampler.reset();
            }
        }
    }

    private class PhotonWorker extends Thread {
        int howMany;
        double searchRadius;
        Sampler sampler;
        HitMap hitMap;

        private PhotonWorker(int howMany, double searchRadius, Sampler sampler, HitMap hitMap) {
            this.howMany = howMany;
            this.searchRadius = searchRadius;
            this.sampler = sampler;
            this.hitMap = hitMap;
        }

        @Override
        public void run() {
            tracePhotons(howMany, hitMap, searchRadius, sampler);
        }
    }

    Sampler[] samplers = new Sampler[] {new RandomSampler(1), new RandomSampler(2), new RandomSampler(3)};
    Sampler[] photonSamplers = new Sampler[] {new RandomSampler(1), new RandomSampler(2), new RandomSampler(3)};

    @Override
    public void run() {
        Vec extent = scene.bvh.getMax().sub(scene.bvh.getMin());
        double searchRadius = ((extent.x + extent.y + extent.z) / 3) /
                ((scene.film.getWidth() + scene.film.getHeight()) / 2) * 3;
        double alpha = 0.7;
        int iter = 0;

        pixels = new int[scene.film.getPixelCount()][2];

            int pixel = 0;
            for (int y = 0; y < scene.film.getHeight(); y++) {
                for (int x = 0; x < scene.film.getWidth(); x++) {
                    pixels[pixel++] = new int[] {x, y};
                }
            }

        int photonsPerPass = 1000000;

        while (true) {
            Sampler.shuffle(pixels, random);

            HitMap hitMap = new HitMap(scene.bvh.getMin(), scene.bvh.getMax(), scene.film.getPixelCount(), 2 * searchRadius);

            Sample pixelSample = scene.filter.transform(pixelSampler.getSample());
            pixelSampler.reset();

        HitPointsWorker[] hitPointsWorkers = new HitPointsWorker[2];
        for (int i = 0; i < hitPointsWorkers.length; i++) {
            hitPointsWorkers[i] = new HitPointsWorker(i * scene.film.getPixelCount() / hitPointsWorkers.length,
                    (i + 1) * scene.film.getPixelCount() / hitPointsWorkers.length, pixelSample, samplers[i], hitMap);
            hitPointsWorkers[i].start();
        }

                try {
            for (int i = 0; i < hitPointsWorkers.length; i++)
                    hitPointsWorkers[i].join();
                } catch (InterruptedException e) {
                }



            PhotonWorker[] photonWorkers = new PhotonWorker[2];
            for (int i = 0; i < photonWorkers.length; i++) {
                photonWorkers[i] = new PhotonWorker(photonsPerPass / photonWorkers.length, searchRadius, photonSamplers[i], hitMap);
                photonWorkers[i].start();
            }

            try {
                for (int i = 0; i < photonWorkers.length; i++)
                    photonWorkers[i].join();
            } catch (InterruptedException e) {
            }
//            for (int i = 0; i < scene.film.getPixelCount(); i++) {
//                double fx = (double) (pixels[i][0] + 0.5 + pixelSample.getX()) / scene.film.getWidth();
//                double fy = (double) (pixels[i][1] + 0.5 + pixelSample.getY()) / scene.film.getHeight();
////                pixels[i][0] = (int) (pixels[i][0] + 0.5 + pixelSample[0]);
////                pixels[i][1] = (int) (pixels[i][1] + 0.5 + pixelSample[1]);
//                Ray ray = scene.camera.getRay(fx, fy, pathSampler);
//                traceRay(ray, hitMap, pixels[i][0], pixels[i][1], pathSampler);
//                pathSampler.reset();
//            }

// film.getHeight(); y++) {
//                for (int x = 0; x < scene.film.getWidth(); x++) {
////                    double a = (x + 0.5 + pixelSample[0]) / scene.film.getWidth();
////                    double b = (y + 0.5 + pixelSample[0]) / scene.film.getWidth();
//                    double a = (x + 0.5) / scene.film.getWidth();
//                    double b = (y + 0.5) / scene.film.getHeight();
////                    pathSampler.getSample();
////                    pathSampler.getSample();
//                    Ray ray = scene.camera.getRay(a, b, pathSampler);
//                    traceRay(ray, hitMap, x, y, pathSampler);
//                    pathSampler.reset();
//                }
//            }

//            tracePhotons(100000, hitMap, searchRadius, photonSampler);

            hitMap.printHitpoints(scene.film);

            searchRadius *= Math.sqrt((iter + alpha) / (iter + 1));
            iter++;
        }
    }

    private void traceRay(Ray ray, HitMap hitMap, int x, int y, Sampler sampler) {
        Col w = new Col(1);
        Col l = new Col(0);

        for (int depth = 0; depth < 10; depth++) {
            HitData hit = new HitData();
            if (!scene.bvh.hit(ray, hit))
                break;

            l = l.add(w.mul(hit.primitive.getMat().getEmittance()));

            Brdf brdf = hit.primitive.getMat().getBrdf(ray, hit, false, sampler);

            if (brdf.isDiffuse()) {
                hitMap.add(new HitPoint(x, y, hit.pos, hit.nors, l));
                return;
            }

            if (brdf.isAbsorptive())
                break;

            w = w.mul(brdf.getScale());

            if (depth > 0) {
                double sp = 0.8;
                if (sampler.get1dSample() > sp)
                    break;
                w.div(sp);
            }

            ray = new Ray(hit.pos, brdf.getDir());
        }

        scene.film.store(x, y, l.r, l.g, l.b);
    }

    void tracePhotons(int photonCount, HitMap hitMap, double rad, Sampler sampler) {
        for (int i = 0; i < photonCount; i++) {
            Primitive light = scene.lights[(int) (sampler.get1dSample() * scene.lights.length)];
            Vec[] sample = light.sample(sampler.getSample());

            Vec out = Diffuse.getCosineWeightedDir(new OBasis(sample[1]), sampler.getSample());
            Ray particle = new Ray(sample[0], out);

            double norm = Math.PI * scene.lights.length * light.getArea();
            Col flux = light.getMat().getEmittance().mul(norm);

            for (int depth = 0; depth < 10; depth++) {
                HitData hit = new HitData();
                if (!scene.bvh.hit(particle, hit))
                    break;

                Brdf brdf = hit.primitive.getMat().getBrdf(particle, hit, true, sampler);

                if (brdf.isAbsorptive())
                    break;

                flux = flux.mul(brdf.getScale());

                if (brdf.isDiffuse()) {
                    hitMap.addRadiance(hit.pos, hit.nors, flux.div(photonCount), rad);
                }

//                if (depth > 0) {
                double sp = 0.8;
                if (sampler.get1dSample() > sp)
                    continue;
                flux.div(sp);
//                }

                particle = new Ray(hit.pos, brdf.getDir());
            }

            sampler.reset();
        }
    }
}

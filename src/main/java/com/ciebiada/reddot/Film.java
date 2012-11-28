/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.filter.Filter;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Utils;

import java.awt.image.BufferedImage;

public class Film {

    private final static int FILTER_RES = 128;
    private final static int FILTER_TABLE_SIZE = FILTER_RES + 1;

    private int samples;

    private int width, height;
    private Col[] pixels;
    private float[] weights;
    private float[] filterTable;

    private float filterSize;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Film(int width, int height, Filter filter) {
        this.width = width;
        this.height = height;
        filterSize = filter.getSize();

        pixels = new Col[width * height];
        weights = new float[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Col(0);
            weights[i] = 0;
        }

        filterTable = new float[FILTER_TABLE_SIZE * FILTER_TABLE_SIZE];
        for (int x = 0; x < FILTER_TABLE_SIZE; x++) {
            for (int y = 0; y < FILTER_TABLE_SIZE; y++) {
                filterTable[y * FILTER_TABLE_SIZE + x] = filter.get(
                        (x + 0.5f) * filterSize / FILTER_RES,
                        (y + 0.5f) * filterSize / FILTER_RES);
            }
        }

    }

    public void store(float a, float b, Col c) {
        float sx = a * width;
        float sy = b * height;

        int x0 = Utils.max(0, Utils.simpleCeil(sx - filterSize));
        int y0 = Utils.max(0, Utils.simpleCeil(sy - filterSize));

        int x1 = Utils.min(width - 1, Utils.simpleFloor(sx + filterSize));
        int y1 = Utils.min(height - 1, Utils.simpleFloor(sy + filterSize));

        for (int y = y0; y <= y1; ++y) {
            for (int x = x0; x <= x1; ++x) {
                int fx = (int) (Utils.abs(x - sx) / filterSize * FILTER_RES);
                int fy = (int) (Utils.abs(y - sy) / filterSize * FILTER_RES);
                float weight = filterTable[fy * FILTER_TABLE_SIZE + fx];

                int idx = y * width + x;
                pixels[idx].addSet(c.mul(weight));
                weights[idx] += weight;
            }
        }

        samples++;
    }

    public int getSamples() {
        return samples;
    }

    public BufferedImage getImage() {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        float logsum = 0;
        float l2white = 0;
        int pixelsCount = 0;
        for (int i = 0; i < width * height; i++) {
            Col pix = pixels[i].mul(1 / weights[i]);
            if (pix.lum() < Utils.EPS)
                continue;
            logsum += Math.log(pix.lum());
            if (pix.lum() > l2white)
                l2white = pix.lum();
            ++pixelsCount;
        }
        l2white *= l2white;

        float key = (float) Math.exp(logsum / (pixelsCount));
        float a = 0.18f;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int idx = y * width + x;
                Col pix = pixels[idx].mul(1 / weights[idx]);
                float l = a / key * pix.lum();
                float ld = (l * (1 + l / l2white)) / (1 + l);
//                bi.setRGB(x, height - 1 - y, pix.mul(ld / pix.lum()).toInt());
                bi.setRGB(x, height - 1 - y, pix.toInt());
            }
        }

        return bi;
    }
}

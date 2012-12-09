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

    private final int width, height;
    private final double[] rgb;
    private final double[] weights;

    private final double filterSize;
    private final double[] filterTable;
    private final static int FILTER_RES = 128;
    private final static int FILTER_TABLE_SIZE = FILTER_RES + 1;

    private long samples;

    public Film(int width, int height, Filter filter) {
        this.width = width;
        this.height = height;
        filterSize = filter.getSize();

        rgb = new double[width * height * 3];
        weights = new double[width * height];

        filterTable = new double[FILTER_TABLE_SIZE * FILTER_TABLE_SIZE];
        for (int x = 0; x < FILTER_TABLE_SIZE; x++) {
            for (int y = 0; y < FILTER_TABLE_SIZE; y++) {
                filterTable[y * FILTER_TABLE_SIZE + x] =
                        filter.get((x + 0.5f) * filterSize / FILTER_RES,
                                (y + 0.5f) * filterSize / FILTER_RES);
            }
        }

    }

    public void store(double a, double b, Col c) {
        double sx = a * width;
        double sy = b * height;

        int x0 = Math.max(0, Utils.simpleCeil(sx - filterSize));
        int y0 = Math.max(0, Utils.simpleCeil(sy - filterSize));

        int x1 = Math.min(width - 1, Utils.simpleFloor(sx + filterSize));
        int y1 = Math.min(height - 1, Utils.simpleFloor(sy + filterSize));

        for (int y = y0; y <= y1; ++y) {
            for (int x = x0; x <= x1; ++x) {
                int fx = (int) (Math.abs(x - sx) / filterSize * FILTER_RES);
                int fy = (int) (Math.abs(y - sy) / filterSize * FILTER_RES);
                double weight = filterTable[fy * FILTER_TABLE_SIZE + fx];

                int idx = y * width + x;

                Col pixel = c.mul(weight);
                rgb[idx * 3] += pixel.r;
                rgb[idx * 3 + 1] += pixel.g;
                rgb[idx * 3 + 2] += pixel.b;

                weights[idx] += weight;
            }
        }

        samples++;
    }

    public long getSamples() {
        return samples;
    }

    public BufferedImage getImage() {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double logsum = 0;
        double l2white = 0;
        int pixelsCount = 0;
        for (int i = 0; i < width * height; i++) {
            Col pix = new Col(rgb[i * 3], rgb[i * 3 + 1], rgb[i * 3 + 2]).mul(1 / weights[i]);
            if (pix.lum() < Utils.EPS)
                continue;
            logsum += Math.log(pix.lum());
            if (pix.lum() > l2white)
                l2white = pix.lum();
            ++pixelsCount;
        }
        l2white *= l2white;

        double key = Math.exp(logsum / (pixelsCount));
        double a = 0.18f;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int idx = y * width + x;
                Col pix = new Col(rgb[idx * 3], rgb[idx * 3 + 1], rgb[idx * 3 + 2]).mul(1 / weights[idx]);
                double l = a / key * pix.lum();
                double ld = (l * (1 + l / l2white)) / (1 + l);
                bi.setRGB(x, height - 1 - y, pix.mul(ld / pix.lum()).toInt());
//                bi.setRGB(x, height - 1 - y, pix.toInt());
            }
        }

        return bi;
    }
}

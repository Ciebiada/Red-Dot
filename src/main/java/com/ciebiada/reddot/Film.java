/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Utils;

import java.awt.image.BufferedImage;

public class Film {

    private final int width, height;
    private final int pixelCount;

    private final double[] rgb;

    private final double[] weights;
    private final BufferedImage bi;

    public Film(int width, int height) {
        this.width = width;
        this.height = height;
        pixelCount = width * height;

        rgb = new double[width * height * 3];
        weights = new double[width * height];

        bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelCount() {
        return pixelCount;
    }

    public void store(int x, int y, double r, double g, double b) {
        int idx = y * width + x;

        rgb[3 * idx] += r;
        rgb[3 * idx + 1] += g;
        rgb[3 * idx + 2] += b;

        weights[idx]++;
    }

    private Col getPixel(int i) {
        double weight = weights[i];
        if (weight == 0)
            weight = 1;
        return new Col(rgb[i * 3], rgb[i * 3 + 1], rgb[i * 3 + 2]).div(weight);
    }

    public BufferedImage getImage() {
        double logsum = 0;
        double l2white = 0;

        for (int i = 0; i < pixelCount; i++) {
            Col pix = getPixel(i);
            double lum = pix.lum();
            logsum += Math.log(lum + Utils.EPS);
            if (lum > l2white)
                l2white = lum;
        }

        double key = Math.exp(logsum / pixelCount);
        l2white *= l2white;

        double a = 0.18f;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int idx = y * width + x;
                Col pix = getPixel(idx);
                double lum = pix.lum();
                double l = a / key * lum;
                double ld = (l * (1 + l / l2white)) / (1 + l);
                bi.setRGB(x, height - 1 - y, pix.mul(ld / lum).toInt());
//                bi.setRGB(x, height - 1 - y, pix.toInt());
            }
        }

        return bi;
    }
}

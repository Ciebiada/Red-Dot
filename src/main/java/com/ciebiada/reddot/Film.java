/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot;

import com.ciebiada.reddot.filter.Filter;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Sample;

import java.awt.image.BufferedImage;

public class Film {

    public int width, height;
    public int pixelCount;

    private Filter filter;

    private Col[] pixels;
    private float[] weights;

    public Film(int width, int height, Filter filter) {
        this.width = width;
        this.height = height;

        pixelCount = width * height;

        this.filter = filter;

        pixels = new Col[pixelCount];
        weights = new float[pixelCount];
        for (int i = 0; i < pixelCount; i++) {
            pixels[i] = new Col(0);
        }
    }

    public void store(Sample cameraSample, Col col) {
        float sx = cameraSample.x * width;
        float sy = cameraSample.y * height;

        int x1 = Math.max(0, (int) (sx - filter.getSize()));
        int y1 = Math.max(0, (int) (sy - filter.getSize()));

        int x2 = Math.min(width - 1, (int) (sx + filter.getSize()) + 1);
        int y2 = Math.min(height - 1, (int) (sy + filter.getSize()) + 1);

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                float weight = filter.fun((x + 0.5f) - sx, (y + 0.5f) - sy);
                int idx = y * width + x;
                pixels[idx].addSet(col.mul(weight));
                weights[idx] += weight;
            }
        }
    }

    public BufferedImage getImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        float radius = Math.max(width, height) * 0.01f;
        float mix = 0.1f;

        Col[] bloom = new Col[pixelCount];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int idx = y * width + x;

                int x1 = Math.max((int) Math.floor(x - radius), 0);
                int y1 = Math.max((int) Math.floor(y - radius), 0);
                int x2 = Math.min((int) Math.ceil(x + radius), width - 1);
                int y2 = Math.min((int) Math.ceil(y + radius), height - 1);

                Col sum = new Col(0);

                for (int fx = x1; fx <= x2; fx++) {
                    for (int fy = y1; fy <= y2; fy++) {
                        int dx = x - fx;
                        int dy = y - fy;
                        float dist = (float) Math.sqrt(dx * dx + dy * dy);
                        if (dist <= radius) {
                            int idx2 = fy * width + fx;
                            sum.addSet(pixels[idx2].div(weights[idx2]));
                        }
                    }
                }

                bloom[idx] = sum.div((float) Math.PI * radius * radius);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x;
                Col c = bloom[idx].mul(mix).add(pixels[idx].div(weights[idx]).mul(1 - mix));
                image.setRGB(x, height - y - 1, c.toInt());
            }
        }

        return image;
    }
}

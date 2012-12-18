/*
 * Copyright (c) 2012, by Michal Ciebiada
 * This is not open source. Redistribution in any form is forbidden.
 */

package com.ciebiada.reddot.tracer;

import com.ciebiada.reddot.Film;
import com.ciebiada.reddot.math.Col;
import com.ciebiada.reddot.math.Vec;

import java.util.ArrayList;
import java.util.List;

public class HitMap {

    private final Vec min, max;
    private final int hashSize;
    private final double cellSizeInv;
    private final Cell[] cells;

    public HitMap(Vec min, Vec max, int hashSize, double cellSize) {
        this.min = min;
        this.max = max;
        this.hashSize = hashSize;
        cellSizeInv = 1 / cellSize;

        cells = new Cell[hashSize];
        for (int i = 0; i < hashSize; i++)
            cells[i] = new Cell();
    }

    public void add(HitPoint hitPoint) {
        Vec idx = hitPoint.pos.sub(min).mul(cellSizeInv);
        cells[hash((int) idx.x, (int) idx.y, (int) idx.z)].hitPoints.add(hitPoint);
    }

    public void addRadiance(Vec point, Vec nor, Col flux, double rad) {
        Vec idx1 = point.sub(new Vec(rad)).sub(min).mul(cellSizeInv);
        Vec idx2 = point.add(new Vec(rad)).sub(min).mul(cellSizeInv);

        double rad2 = rad * rad;

        for (int ix = (int) idx1.x; ix <= (int) idx2.x; ++ix) {
            for (int iy = (int) idx1.y; iy <= (int) idx2.y; ++iy) {
                for (int iz = (int) idx1.z; iz <= (int) idx2.z; ++iz) {
                    Cell cell = cells[hash(ix, iy, iz)];
                    for (HitPoint hitPoint : cell.hitPoints) {
                        Vec toHitPoint = point.sub(hitPoint.pos);
                        double dist2 = toHitPoint.dot(toHitPoint);

                        if (nor.dot(hitPoint.nor) > 1e-3 && dist2 < rad2) {
                            Col radiance = flux.div(Math.PI * rad2);
                            hitPoint.r += radiance.r;
                            hitPoint.g += radiance.g;
                            hitPoint.b += radiance.b;
                        }
                    }
                }
            }
        }
    }

    private int hash(int ix, int iy, int iz) {
        return (int) ((((ix * 73856093) ^ (iy * 19349663) ^ (iz * 83492791)) & 0xffffffffl) % hashSize);
    }

    public void printHitpoints(Film film) {
        for (Cell cell : cells) {
            for (HitPoint hitPoint : cell.hitPoints) {
                film.store(hitPoint.x, hitPoint.y, hitPoint.r, hitPoint.g, hitPoint.b);
            }
        }
    }

    private class Cell {
        public List<HitPoint> hitPoints = new ArrayList<HitPoint>();
    }
}

package com.ciebiada.reddot.geometry;

import com.ciebiada.reddot.material.Material;
import com.ciebiada.reddot.math.Vec;

public class TriangleRaw {

    private Vec a, b, c;

    private Material material;

    public TriangleRaw(Vec a, Vec b, Vec c, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.material = material;
    }

    public Triangle getTriangle() {
        return new Triangle(a, b, c, material);
    }

    public Vec getMin() {
        return new Vec(
                Math.min(a.x, Math.min(b.x, c.x)),
                Math.min(a.y, Math.min(b.y, c.y)),
                Math.min(a.z, Math.min(b.z, c.z)));
    }

    public Vec getMax() {
        return new Vec(
                Math.max(a.x, Math.max(b.x, c.x)),
                Math.max(a.y, Math.max(b.y, c.y)),
                Math.max(a.z, Math.max(b.z, c.z)));
    }

    public Vec[][] splitBoundingBox(float pos, int axis) {
        Vec outsidePoint;
        Vec[] otherPoints;
        boolean outsideOnTheLeft;

        if (a.get(axis) > pos) {
            if (b.get(axis) > pos) {
                outsidePoint = c;
                otherPoints = new Vec [] {a, b};
                outsideOnTheLeft = true;
            } else if (c.get(axis) > pos) {
                outsidePoint = b;
                otherPoints = new Vec[] {a, c};
                outsideOnTheLeft = true;
            } else {
                outsidePoint = a;
                otherPoints = new Vec[] {b ,c};
                outsideOnTheLeft = false;
            }
        } else {
            if (b.get(axis) < pos) {
                outsidePoint = c;
                otherPoints = new Vec[] {a, b};
                outsideOnTheLeft = false;
            } else if (c.get(axis) < pos) {
                outsidePoint = b;
                otherPoints = new Vec[] {a, c};
                outsideOnTheLeft = false;
            } else {
                outsidePoint = a;
                otherPoints = new Vec[] {b, c};
                outsideOnTheLeft = true;
            }
        }

        Vec line1 = otherPoints[0].sub(outsidePoint).norm();
        float t1 = (pos - outsidePoint.get(axis)) / line1.get(axis);
        Vec ip1 = outsidePoint.add(line1.mul(t1));

        Vec line2 = otherPoints[1].sub(outsidePoint).norm();
        float t2 = (pos - outsidePoint.get(axis)) / line2.get(axis);
        Vec ip2 = outsidePoint.add(line2.mul(t2));

//        Vec leftMin = new Vec(getMin(0), getMin(1), getMin(2));
//        Vec rightMin = new Vec(leftMin.x, leftMin.y, leftMin.z);
//        rightMin.set(axis, pos);

        Vec[] bounds1 = getBounds(new Vec[] {outsidePoint, ip1, ip2});
        Vec[] bounds2 = getBounds(new Vec[] {otherPoints[0], otherPoints[1], ip1, ip2});

        return outsideOnTheLeft ? new Vec[][] {bounds1, bounds2} : new Vec[][] {bounds2, bounds1};
    }

    public Vec[] getBounds() {
        return getBounds(new Vec[] {a, b, c});
    }

    private Vec[] getBounds(Vec[] points) {
        Vec min = new Vec(Float.POSITIVE_INFINITY);
        Vec max = new Vec(Float.NEGATIVE_INFINITY);

        for (Vec point : points) {
            if (point.x < min.x) min.x = point.x;
            if (point.y < min.y) min.y = point.y;
            if (point.z < min.z) min.z = point.z;

            if (point.x > max.x) max.x = point.x;
            if (point.y > max.y) max.y = point.y;
            if (point.z > max.z) max.z = point.z;
        }

        return new Vec[] {min, max};
    }
}

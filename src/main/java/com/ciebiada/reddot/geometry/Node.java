package com.ciebiada.reddot.geometry;

public class Node {

    public float split;

    private int data;

    public void setSplit(float split) {
        this.split = split;
    }

    public void setAxis(int axis) {
        data = (data & 0xfffffffc) + (axis & 3);
    }

    public int getAxis() {
        return data & 3;
    }

    public void setOffset(int offset) {
        data = (offset << 2) + (data & 3);
    }

    public int getOffset() {
        return data >> 2;
    }

    public void setLeaf() {
        data = (data & 0xfffffffc) + 3;
    }

    public boolean isLeaf() {
        return (data & 3) == 3;
    }
}

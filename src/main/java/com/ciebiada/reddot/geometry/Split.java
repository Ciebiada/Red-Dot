package com.ciebiada.reddot.geometry;

public class Split {
    private float pos;
    private int axis;
    private int leftCount, rightCount;
    private int startOffset, endOffset;
    private float cost;

    public Split(float pos, int axis, int leftCount, int rightCount, int startOffset, int endOffset, float cost) {
        this.pos = pos;
        this.axis = axis;
        this.leftCount = leftCount;
        this.rightCount = rightCount;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.cost = cost;
    }

    public float getPos() {
        return pos;
    }

    public int getAxis() {
        return axis;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public float getCost() {
        return cost;
    }

    public boolean isValid() {
        return true;
    }
}
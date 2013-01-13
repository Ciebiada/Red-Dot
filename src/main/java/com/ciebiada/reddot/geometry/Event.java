package com.ciebiada.reddot.geometry;

public class Event implements Comparable<Event> {

    private float pos;
    private int data;

    public Event(float pos, int type, int tri) {
        this.pos = pos;
        data = (tri << 1) + (type & 1);
    }

    public float getPos() {
        return pos;
    }

    public int getType() {
        return data & 1;
    }

    public int getTri() {
        return data >> 1;
    }

    @Override
    public int compareTo(Event e) {
        return (getPos() < e.getPos()) ? -1 : (getPos() > e.getPos()) ? 1 :
                getType() - e.getType();
    }
}

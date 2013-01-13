package com.ciebiada.reddot.geometry;

import com.ciebiada.reddot.math.Ray;
import com.ciebiada.reddot.math.Vec;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KDTree {

    private Logger logger = Logger.getLogger(this.getClass());

    private final int MAX_NODE_SIZE = 4;

    private final int MAX_DEPTH = 24;

    private final float TRAVERSAL_COST = 1;
    private final float INTERSECTION_COST = 0.2f;

    private BBox bbox;
    private ArrayList<Node> tree;
    private List<List<TriangleRaw>> triangles;

    public KDTree(TriangleRaw[] tris) {
        tree = new ArrayList<Node>();
        triangles = new ArrayList<List<TriangleRaw>>();

        Node root = new Node();
        tree.add(root);

        Event[] eventsx = new Event[tris.length * 2];
        Event[] eventsy = new Event[tris.length * 2];
        Event[] eventsz = new Event[tris.length * 2];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < tris.length; i++) {
            TriangleRaw tri = tris[i];

            Vec min = tri.getMin();
            Vec max = tri.getMax();
            eventsx[2 * i] = new Event(min.x, 0, i);
            eventsx[2 * i + 1] = new Event(max.x, 1, i);
            eventsy[2 * i] = new Event(min.y, 0, i);
            eventsy[2 * i + 1] = new Event(max.y, 1, i);
            eventsz[2 * i] = new Event(min.z, 0, i);
            eventsz[2 * i + 1] = new Event(max.z, 1, i);
        }
        ;

        Arrays.sort(eventsx);
        Arrays.sort(eventsy);
        Arrays.sort(eventsz);

        logger.debug("Sorted events in: " + (System.currentTimeMillis() - startTime) / 1000.0f);

        Vec[] bounds = getBounds(tris);

        buildTree(root, eventsx, eventsy, eventsz, tris, new byte[tris.length], bounds[0], bounds[1], 0);

        logger.debug("Total build time: " + (System.currentTimeMillis() - startTime) / 1000.0f);

        bbox = new BBox(bounds[0].sub(new Vec(1e-5f)), bounds[1].add(new Vec(1e-5f)));
    }

    private float costFunction(int leftCount, int rightCount, float leftArea, float rightArea, float totalAreaInv) {
        float emptyBonus = (leftCount == 0) || (rightCount == 0) ? 1 : 1;

        return TRAVERSAL_COST + emptyBonus * INTERSECTION_COST *
                (leftArea * leftCount + rightArea * rightCount) * totalAreaInv;
    }

    private Split scanEvents(Event[] events, int axis, int trisCount, Vec nodeMin, Vec nodeMax, Vec size,
                             float totalAreaInv, Split bestSplit) {
        int nl = 0;
        int nr = trisCount;

        for (int i = 0; i < events.length;) {
            Event event = events[i];
            int startOffset = i;

            int pstart = 0;
            int pend = 0;

            while (i < events.length && events[i].getPos() == event.getPos() && events[i].getType() == 0) {
                pstart++;
                i++;
            }

            while (i < events.length && events[i].getPos() == event.getPos() && events[i].getType() == 1) {
                pend++;
                i++;
            }

            nr -= pend;

            if (event.getPos() > nodeMin.get(axis) && event.getPos() < nodeMax.get(axis)) {
                float leftArea, rightArea;

                switch (axis) {
                    case 0: {
                        float leftWidth = event.getPos() - nodeMin.x;
                        float rightWidth = nodeMax.x - event.getPos();
                        leftArea = (size.y * size.z) + leftWidth * (size.y + size.z);
                        rightArea = (size.y * size.z) + rightWidth * (size.y + size.z);
                        break;
                    } case 1: {
                        float leftWidth = event.getPos() - nodeMin.y;
                        float rightWidth = nodeMax.y - event.getPos();
                        leftArea = (size.z * size.x) + leftWidth * (size.z + size.x);
                        rightArea = (size.z * size.x) + rightWidth * (size.z + size.x);
                        break;
                    } default: {
                        float leftWidth = event.getPos() - nodeMin.z;
                        float rightWidth = nodeMax.z - event.getPos();
                        leftArea = (size.x * size.y) + leftWidth * (size.x + size.y);
                        rightArea = (size.x * size.y) + rightWidth * (size.x + size.y);
                        break;
                    }
                }

                float cost = costFunction(nl, nr, leftArea, rightArea, totalAreaInv);

                if (cost < bestSplit.getCost()) {
                    bestSplit = new Split(event.getPos(), axis, nl, nr, startOffset, i, cost);
                }
            }

            nl += pstart;
        }

        return bestSplit;
    }

    private Split getSplit(Event[] eventsx, Event[] eventsy, Event[] eventsz, int trisCount, Vec nodeMin, Vec nodeMax) {
        Vec size = nodeMax.sub(nodeMin);
        float totalAreaInv = 1 / (size.x * size.y + size.x * size.z + size.z * size.y);

        Split bestSplit = new SplitInvalid(INTERSECTION_COST * trisCount);

        bestSplit = scanEvents(eventsx, 0, trisCount, nodeMin, nodeMax, size, totalAreaInv, bestSplit);
        bestSplit = scanEvents(eventsy, 1, trisCount, nodeMin, nodeMax, size, totalAreaInv, bestSplit);
        bestSplit = scanEvents(eventsz, 2, trisCount, nodeMin, nodeMax, size, totalAreaInv, bestSplit);

        return bestSplit;
    }

    private void classifyTriangles(byte[] classify, Event[] events, Split split) {
        for (Event event : events) {
            if (event.getType() == 0) {
                classify[event.getTri()] = 0;
            }
        }

        for (int i = 0; i < split.getStartOffset(); i++) {
            if (events[i].getType() == 0) {
                classify[events[i].getTri()] |= 1;
            }
        }

        for (int i = split.getEndOffset(); i < events.length; i++) {
            if (events[i].getType() == 1) {
                classify[events[i].getTri()] |= 2;
            }
        }
    }

    private void splitEvents(Event[] events, Event[] eventsLeft, Event[] eventsRight, byte[] classify) {
        int leftPtr = 0;
        int rightPtr = 0;

        for (Event event : events) {
            if ((classify[event.getTri()] & 1) == 1) {
                eventsLeft[leftPtr++] = event;
            } if ((classify[event.getTri()] & 2) == 2) {
                eventsRight[rightPtr++] = event;
            }
        }
    }

    private void buildTree(Node node, Event[] eventsx, Event[] eventsy, Event[] eventsz,
                           TriangleRaw[] tris, byte[] classify, Vec nodeMin, Vec nodeMax, int depth) {
        int trisCount = eventsx.length / 2;

        if (trisCount > MAX_NODE_SIZE && depth < MAX_DEPTH) {
            Split split = getSplit(eventsx, eventsy, eventsz, trisCount, nodeMin, nodeMax);

            if (split.isValid()) {
                int axis = split.getAxis();
                Event[] events = (axis == 0) ? eventsx : (axis == 1) ? eventsy : eventsz;

                classifyTriangles(classify, events, split);

                Event[] eventsxLeft = new Event[split.getLeftCount() * 2];
                Event[] eventsyLeft = new Event[split.getLeftCount() * 2];
                Event[] eventszLeft = new Event[split.getLeftCount() * 2];
                Event[] eventsxRight = new Event[split.getRightCount() * 2];
                Event[] eventsyRight = new Event[split.getRightCount() * 2];
                Event[] eventszRight = new Event[split.getRightCount() * 2];

                splitEvents(eventsx, eventsxLeft, eventsxRight, classify);
                splitEvents(eventsy, eventsyLeft, eventsyRight, classify);
                splitEvents(eventsz, eventszLeft, eventszRight, classify);

                node.setAxis(split.getAxis());
                node.setSplit(split.getPos());

                Vec leftMax = new Vec(nodeMax.x, nodeMax.y, nodeMax.z);
                leftMax.set(split.getAxis(), split.getPos());
                Vec rightMin = new Vec(nodeMin.x, nodeMin.y, nodeMin.z);
                rightMin.set(split.getAxis(), split.getPos());

                Node leftChild = new Node();
                Node rightChild = new Node();
                node.setOffset(tree.size());
                tree.add(leftChild);
                tree.add(rightChild);

                buildTree(leftChild, eventsxLeft, eventsyLeft, eventszLeft, tris, classify, nodeMin, leftMax, depth + 1);
                buildTree(rightChild, eventsxRight, eventsyRight, eventszRight, tris, classify, rightMin, nodeMax, depth + 1);

                return;
            }
        }

        node.setLeaf();
        node.setOffset(triangles.size());

        List<TriangleRaw> leafTris = new ArrayList<TriangleRaw>(trisCount);

        for (Event event : eventsx) {
            if (event.getType() == 0) {
                leafTris.add(tris[event.getTri()]);
            }
        }

        triangles.add(leafTris);
    }

    private List<Event> mergeEvents(List<Event> events1, List<Event> events2) {
        int i = 0;
        int j = 0;
        int size = events1.size() + events2.size();
        List<Event> result = new ArrayList<Event>(size);
        while (result.size() < size) {
            if (i < events1.size() && (j >= events2.size() || events1.get(i).compareTo(events2.get(j)) == -1)) {
                result.add(events1.get(i++));
            } else {
                result.add(events2.get(j++));
            }
        }

        return result;
    }

    private Vec[] getBounds(TriangleRaw[] tris) {
        Vec worldMin = new Vec(Float.POSITIVE_INFINITY);
        Vec worldMax = new Vec(Float.NEGATIVE_INFINITY);

        for (TriangleRaw tri : tris) {
            Vec min = tri.getMin();
            Vec max = tri.getMax();

            worldMin.x = Math.min(worldMin.x, min.x);
            worldMin.y = Math.min(worldMin.y, min.y);
            worldMin.z = Math.min(worldMin.z, min.z);

            worldMax.x = Math.max(worldMax.x, max.x);
            worldMax.y = Math.max(worldMax.y, max.y);
            worldMax.z = Math.max(worldMax.z, max.z);
        }

        return new Vec[] {worldMin, worldMax};
    }

    public boolean rayIntersection(Ray ray, StackElem[] stack) {
        if (!bbox.intersect(ray)) {
            return false;
        }

        float t;

        float tmin = ray.tmin;
        float tmax = ray.tmax;

        Node current;
        int near, far;

        stack[0].tmin = tmin;
        stack[0].tmax = tmax;
        stack[0].node = 0;

        int ptr = 1;

        while (ptr > 0) {
            ptr--;
            tmin = stack[ptr].tmin;
            tmax = stack[ptr].tmax;
            current = tree.get(stack[ptr].node);
            while (!current.isLeaf()) {
                float dist;

                switch (current.getAxis()) {
                    case 0:
                        dist = current.split - ray.orig.x;
                        t = dist * ray.dirxInv;
                        break;
                    case 1:
                        dist = current.split - ray.orig.y;
                        t = dist * ray.diryInv;
                        break;
                    default:
                        dist = current.split - ray.orig.z;
                        t = dist * ray.dirzInv;
                        break;
                }

                if (dist < 0) {
                    far = current.getOffset();
                    near = far + 1;
                } else {
                    near = current.getOffset();
                    far = near + 1;
                }

                if (t >= tmax || t <= 0) {
                    current = tree.get(near);
                } else if (t <= tmin) {
                    current = tree.get(far);
                } else {
                    stack[ptr].tmin = t;
                    stack[ptr].tmax = tmax;
                    stack[ptr].node = far;
                    ptr++;

                    current = tree.get(near);
                    tmax = t;
                }
            }

            boolean intersects = false;

            ray.tmax = tmax;
            List<TriangleRaw> tris = triangles.get(current.getOffset());
            for (TriangleRaw tri : tris)
                if (tri.rayIntersection(ray))
                    intersects = true;

            if (intersects) return true;
        }

        return false;
    }

    public boolean shadowRayIntersection(Ray ray, StackElem[] stack) {
        float tmin = 0;
        float tmax = ray.tmax;

        float t;

        Node current;
        int near, far;

        stack[0].tmin = tmin;
        stack[0].tmax = tmax;
        stack[0].node = 0;

        int ptr = 1;

        while (ptr > 0) {
            ptr--;
            tmin = stack[ptr].tmin;
            tmax = stack[ptr].tmax;
            current = tree.get(stack[ptr].node);
            while (!current.isLeaf()) {
                float dist;

                switch (current.getAxis()) {
                    case 0:
                        dist = current.split - ray.orig.x;
                        t = dist * ray.dirxInv;
                        break;
                    case 1:
                        dist = current.split - ray.orig.y;
                        t = dist * ray.diryInv;
                        break;
                    default:
                        dist = current.split - ray.orig.z;
                        t = dist * ray.dirzInv;
                        break;
                }

                if (dist < 0) {
                    far = current.getOffset();
                    near = far + 1;
                } else {
                    near = current.getOffset();
                    far = near + 1;
                }

                if (t >= tmax || t <= 0) {
                    current = tree.get(near);
                } else if (t <= tmin) {
                    current = tree.get(far);
                } else {
                    stack[ptr].tmin = t;
                    stack[ptr].tmax = tmax;
                    stack[ptr].node = far;
                    ptr++;

                    current = tree.get(near);
                    tmax = t;
                }
            }

            ray.tmax = tmax;
            List<TriangleRaw> tris = triangles.get(current.getOffset());
            for (TriangleRaw tri : tris)
                if (tri.shadowRayIntersection(ray))
                    return true;
        }

        return false;
    }
}

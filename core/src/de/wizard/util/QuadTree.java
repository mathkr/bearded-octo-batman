package de.wizard.util;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class QuadTree<T extends Circle> {
        private final static int MAX_BUCKET_SIZE = 10;
        private final static int MAX_DEPTH = 5;

        private Node root;

        public QuadTree(Rectangle bounds) {
                root = new Node(0, bounds);
        }

        public void insert(T elem) {
                root.insert(elem);
        }

        public void remove(T elem) {
                root.remove(elem);
        }

        public Array<T> getRange(Rectangle area, Array<T> res) {
                return root.getRange(area, res);
        }

        public void clear() {
                root.clear();
        }

        private class Node {
                public final Rectangle bounds;
                public final Vector2 center;
                public final Array<T> bucket;
                public final int depth;

                /*
                 * The sub nodes are ordered as follows:
                 *     { SW(0), SE(1), NW(2), NE(3) }
                 *
                 *     +======+======+
                 *     +      +      +
                 *     + NW   + NE   +
                 *     +      +      +
                 *     +    2 +    3 +
                 *     +      +      +
                 *     +======+======+
                 *     +      +      +
                 *     + SW   + SE   +
                 *     +      +      +
                 *     +    0 +    1 +
                 *     +      +      +
                 *     +======+======+
                 */
                public Array<Node> nodes;

                Node(int depth, Rectangle bounds) {
                        this.depth  = depth;
                        this.bounds = bounds;
                        this.center = bounds.getCenter(new Vector2());
                        this.bucket = new Array<>(MAX_BUCKET_SIZE);
                        this.nodes  = new Array<>(4);
                }

                public boolean insert(T elem) {
                        if (!contains(bounds, elem) && depth > 0) {
                                return false;
                        } else if (bucket.size < MAX_BUCKET_SIZE || depth >= MAX_DEPTH) {
                                bucket.add(elem);
                                return true;
                        } else {
                                if (nodes.size == 0) subdivide();

                                int index = getIndex(elem.x, elem.y);
                                boolean res = nodes.get(index).insert(elem);

                                if (!res) {
                                        bucket.add(elem);
                                }

                                return true;
                        }
                }

                private boolean contains(Rectangle r, T e) {
                        return     r.contains(e.x - e.radius, e.y - e.radius)
                                && r.contains(e.x + e.radius, e.y - e.radius)
                                && r.contains(e.x - e.radius, e.y + e.radius)
                                && r.contains(e.x + e.radius, e.y + e.radius);
                }

                private void subdivide() {
                        float halfW = bounds.getWidth() / 2;
                        float halfH = bounds.getHeight() / 2;

                        Rectangle sw = new Rectangle(bounds.x, bounds.y, halfW, halfH);
                        Rectangle se = new Rectangle(center.x, bounds.y, halfW, halfH);
                        Rectangle nw = new Rectangle(bounds.x, center.y, halfW, halfH);
                        Rectangle ne = new Rectangle(center.x, center.y, halfW, halfH);

                        int d = depth + 1;

                        nodes.add(new Node(d, sw));
                        nodes.add(new Node(d, se));
                        nodes.add(new Node(d, nw));
                        nodes.add(new Node(d, ne));

                        Array<T> elements = new Array<>(bucket);
                        bucket.clear();

                        elements.forEach(this::insert);
                }

                public Array<T> getRange(Rectangle range, Array<T> res) {
                        res.addAll(bucket);

                        for (Node node : nodes) {
                                if (node.bounds.overlaps(range)) {
                                        node.getRange(range, res);
                                }
                        }

                        return res;
                }

                public void clear() {
                        bucket.clear();

                        for (Node node : nodes) {
                                node.clear();
                        }

                        nodes.clear();
                }

                private int getIndex(float x, float y) {
                        if (x < center.x) {
                                return (y < center.y) ? 0 : 2;
                        } else {
                                return (y < center.y) ? 1 : 3;
                        }
                }

                public void remove(T elem) {
                        bucket.removeValue(elem, true);

                        int index = getIndex(elem.x, elem.y);
                        if (nodes.size > 0) nodes.get(index).remove(elem);
                }
        }
}

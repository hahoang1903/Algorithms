import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {
    private int size;
    private Node root;
    public KdTree() {
        size = 0;
        root = null;
    }

    private class Node {
        private final double x;
        private final double y;
        private final boolean vertical;
        private Node left;
        private Node right;

        public Node(double x, double y, boolean vertical) {
            this.x = x;
            this.y = y;
            this.vertical = vertical;
            left = null;
            right = null;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (contains(p))
            return;
        if (root == null)
            root = new Node(p.x(), p.y(), true);
        else {
            root = insert(root, p, false);
        }
        size++;
    }

    private Node insert(Node node, Point2D p, boolean vertical) {
        if (node == null)
            return new Node(p.x(), p.y(), vertical);

        if (node.vertical) {
            if (p.x() < node.x)
                node.left = insert(node.left, p, false);
            else
                node.right = insert(node.right, p, false);
        } else {
            if (p.y() < node.y)
                node.left = insert(node.left, p, true);
            else
                node.right = insert(node.right, p, true);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Node node = root;
        while (node != null) {
            if (node.x == p.x() && node.y == p.y()) return true;
            if (node.vertical) {
                if (p.x() < node.x)
                    node = node.left;
                else
                    node = node.right;
            } else {
                if (p.y() < node.y)
                    node = node.left;
                else
                    node = node.right;
            }
        }
        return false;
    }

    public void draw() {
        draw(root, -1);
    }

    private void draw(Node node, double coordinate) {
        if (node == null)
            return;
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.point(node.x, node.y);

        // draw root
        if (coordinate == -1) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(node.x, 0, node.x, 1);
        } else {
            if (node.vertical) {
                StdDraw.setPenColor(Color.RED);
                if (node.y < coordinate)
                    StdDraw.line(node.x, 0, node.x, coordinate);
                else
                    StdDraw.line(node.x, coordinate, node.x, 1);
            } else {
                StdDraw.setPenColor(Color.BLUE);
                if (node.x < coordinate)
                    StdDraw.line(0, node.y, coordinate, node.y);
                else
                    StdDraw.line(coordinate, node.y, 1, node.y);
            }
        }
        double nextCoordinate;
        if (node.vertical)
            nextCoordinate = node.x;
        else
            nextCoordinate = node.y;
        draw(node.left, nextCoordinate);
        draw(node.right, nextCoordinate);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> pointsInRect = new Queue<Point2D>();
        // construct unit square
        RectHV unitSq = new RectHV(0, 0, 1, 1);
        search(root, rect, pointsInRect, unitSq);
        return pointsInRect;
    }

    private void search(Node node, RectHV rect, Queue<Point2D> q, RectHV nodeRect) {
        if (!rect.intersects(nodeRect) || node == null)
            return;
        Point2D p = new Point2D(node.x, node.y);
        if (rect.contains(p))
            q.enqueue(p);
        if (node.vertical) {
            search(node.left, rect, q,
                   new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.x, nodeRect.ymax()));
            search(node.right, rect, q,
                   new RectHV(node.x, nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax()));
        } else {
            search(node.left, rect, q,
                   new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.y));
            search(node.right, rect, q,
                   new RectHV(nodeRect.xmin(), node.y, nodeRect.xmax(), nodeRect.ymax()));
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        Node champion = nearest(root, root, p,
                                new RectHV(0, 0, 1, 1));
        return new Point2D(champion.x, champion.y);
    }

    private Node nearest(Node node, Node champion, Point2D p, RectHV nodeRect) {
        if (node == null)
            return null;
        double championDistance = p.distanceSquaredTo(new Point2D(champion.x, champion.y));
        if (championDistance < nodeRect.distanceSquaredTo(p))
            return null;
        double distance = p.distanceSquaredTo(new Point2D(node.x, node.y));
        Node nearest = champion;
        if (distance < championDistance)
            nearest = node;
        Node temp;
        if (node.vertical && p.x() < node.x) {
            temp = nearest(node.left, nearest, p,
                           new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.x, nodeRect.ymax()));
            if (temp != null &&
                p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
            temp = nearest(node.right, nearest, p,
                           new RectHV(node.x, nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax()));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
        } else if (node.vertical && p.x() >= node.x) {
            temp = nearest(node.right, nearest, p,
                           new RectHV(node.x, nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax()));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
            temp = nearest(node.left, nearest, p,
                           new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.x, nodeRect.ymax()));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
        } else if (!node.vertical && p.y() < node.y) {
            temp = nearest(node.left, nearest, p,
                           new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.y));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
            temp = nearest(node.right, nearest, p,
                           new RectHV(nodeRect.xmin(), node.y, nodeRect.xmax(), nodeRect.ymax()));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
        } else {
            temp = nearest(node.right, nearest, p,
                           new RectHV(nodeRect.xmin(), node.y, nodeRect.xmax(), nodeRect.ymax()));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
            temp = nearest(node.left, nearest, p,
                           new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.y));
            if (temp != null &&
                    p.distanceSquaredTo(new Point2D(temp.x, temp.y)) < p.distanceSquaredTo(new Point2D(nearest.x, nearest.y)))
                nearest = temp;
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}

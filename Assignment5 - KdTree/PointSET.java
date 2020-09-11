import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;
    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        for (Point2D p : points)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Queue<Point2D> pointInRect = new Queue<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p))
                pointInRect.enqueue(p);
        }
        return pointInRect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        double min = points.first().distanceSquaredTo(p);
        Point2D nearestPoint = points.first();
        for (Point2D point : points) {
            double distance = point.distanceSquaredTo(p);
            if (distance < min) {
                min = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {

    }
}

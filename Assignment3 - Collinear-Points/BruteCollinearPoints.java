import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lines;
    private int size = 0;
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();

        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++)
            copy[i] = points[i];
        Arrays.sort(copy);
        lines = new LineSegment[1];
        int length = copy.length;
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    for (int m = k + 1; m < length; m++) {
                        Point p = copy[i];
                        Point q = copy[j];
                        Point r = copy[k];
                        Point s = copy[m];

                        if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(q) == p.slopeTo(s)) {
                            if (size == lines.length)
                                resize(size * 2);

                            LineSegment segment = new LineSegment(p, s);
                            lines[size++] = segment;
                        }
                    }
                }
            }
        }
        resize(size);
    }

    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = lines[i];
        lines = copy;
    }

    public int numberOfSegments() {
        return size;
    }

    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[size];
        for (int i = 0; i < size; i++)
            copy[i] = lines[i];
        return copy;
    }
}

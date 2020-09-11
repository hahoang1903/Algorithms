import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] lines;
    private int size = 0;
    public FastCollinearPoints(Point[] points) {
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
        lines = new LineSegment[1];

        for (int i = 0; i < points.length; i++) {
            int finalI = i;
            Arrays.sort(copy, (p1, p2) -> {
                if (points[finalI].slopeTo(p1) < points[finalI].slopeTo(p2))
                    return -1;
                if (points[finalI].slopeTo(p1) > points[finalI].slopeTo(p2))
                    return 1;
                return p1.compareTo(p2);
            });
            int j = 1;
            int k = 2;
            while (k < points.length) {
                while (k < points.length && points[i].slopeTo(copy[k]) == points[i].slopeTo(copy[j]))
                    k++;
                if (k - j >= 3 && copy[0].compareTo(copy[j]) < 0) {
                    LineSegment line = new LineSegment(copy[0], copy[k - 1]);
                    if (size == lines.length)
                        resize(size * 2);
                    lines[size++] = line;
                }
                j = k;
                k++;
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

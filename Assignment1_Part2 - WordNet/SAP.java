import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (isNotInRange(v, w))
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int length = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i))
                if (bfsV.distTo(i) + bfsW.distTo(i) < length)
                    length = bfsV.distTo(i) + bfsW.distTo(i);
        }

        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (isNotInRange(v, w))
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (bfsV.distTo(i) + bfsW.distTo(i) < length) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (isIllegalArg(v, w))
            throw new IllegalArgumentException();

        if (isEmpty(v) || isEmpty(w))
            return -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int length = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i))
                if (bfsV.distTo(i) + bfsW.distTo(i) < length)
                    length = bfsV.distTo(i) + bfsW.distTo(i);
        }

        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (isIllegalArg(v, w))
            throw new IllegalArgumentException();

        if (isEmpty(v) || isEmpty(w))
            return -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (bfsV.distTo(i) + bfsW.distTo(i) < length) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    private boolean isNotInRange(int v, int w) {
        if (v < 0 || v >= G.V())
            return true;
        if (w < 0 || w >= G.V())
            return true;
        return false;
    }

    private boolean isIllegalArg(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            return true;
        for (Integer vertex : v)
            if (vertex == null || vertex < 0 || vertex >= G.V())
                return true;
        for (Integer vertex : w)
            if (vertex == null || vertex < 0 || vertex >= G.V())
                return true;
        return false;
    }

    private boolean isEmpty(Iterable<Integer> v) {
        for (Integer ignored : v)
            return false;

        return true;
    }
}

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph G;
    private final BreadthFirstDirectedPaths[] bfsArr;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = new Digraph(G);
        bfsArr = new BreadthFirstDirectedPaths[G.V()];
    }

    public int length(int v, int w) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException();
        if (w < 0 || w >= G.V())
            throw new IllegalArgumentException();

        return length(v, w, true);
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException();
        if (w < 0 || w >= G.V())
            throw new IllegalArgumentException();

        return ancestor(v, w, true);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    private int length(int v, int w, boolean clear) {
        if (bfsArr[v] == null)
            bfsArr[v] = new BreadthFirstDirectedPaths(G, v);

        if (bfsArr[w] == null)
            bfsArr[w] = new BreadthFirstDirectedPaths(G, w);

        int length = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (bfsArr[v].hasPathTo(i) && bfsArr[w].hasPathTo(i))
                if (bfsArr[v].distTo(i) + bfsArr[w].distTo(i) < length)
                    length = bfsArr[v].distTo(i) + bfsArr[w].distTo(i);
        }

        if (clear) {
            bfsArr[v] = null;
            bfsArr[w] = null;
        }

        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    private int ancestor(int v, int w, boolean clear) {
        if (bfsArr[v] == null)
            bfsArr[v] = new BreadthFirstDirectedPaths(G, v);

        if (bfsArr[w] == null)
            bfsArr[w] = new BreadthFirstDirectedPaths(G, w);

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsArr[v].hasPathTo(i) && bfsArr[w].hasPathTo(i)) {
                if (bfsArr[v].distTo(i) + bfsArr[w].distTo(i) < length) {
                    length = bfsArr[v].distTo(i) + bfsArr[w].distTo(i);
                    ancestor = i;
                }
            }
        }

        if (clear) {
            bfsArr[v] = null;
            bfsArr[w] = null;
        }

        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (isIllegalArg(v, w))
            throw new IllegalArgumentException();

        int length = Integer.MAX_VALUE;
        for (int i : v) {
            for (int j : w) {
                int tempLength = length(i, j, false);
                if (tempLength != -1 && tempLength < length)
                    length = tempLength;
            }
        }

        return length == Integer.MAX_VALUE ? -1 : length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (isIllegalArg(v, w))
            throw new IllegalArgumentException();

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i : v) {
            for (int j : w) {
                int tempLength = length(i, j, false);
                if (tempLength != -1 && tempLength < length) {
                    length = tempLength;
                    ancestor = ancestor(i, j, false);
                }
            }
        }

        return ancestor;
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
}

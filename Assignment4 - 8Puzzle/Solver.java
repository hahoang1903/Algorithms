import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> solutionSteps;
    private boolean solvable = true;

    private static class Node {
        private final Board board;
        private final Node prev;
        private final int moves;
        private final int priority;

        public Node(Board board, Node prev, int moves, int priority) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.priority = priority;
        }
    }

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        MinPQ<Node> minPQ = new MinPQ<Node>((n1, n2) -> Integer.compare(n1.priority, n2.priority));
        minPQ.insert(new Node(initial, null, 0, initial.manhattan()));
        Node prev = minPQ.delMin();

        Board twinInitial = initial.twin();
        MinPQ<Node> twinMinPQ = new MinPQ<Node>((n1, n2) -> Integer.compare(n1.priority, n2.priority));
        twinMinPQ.insert(new Node(twinInitial, null, 0, twinInitial.manhattan()));
        Node twinPrev = twinMinPQ.delMin();

        while (!prev.board.isGoal() && !twinPrev.board.isGoal()) {
            for (Board b : prev.board.neighbors()) {
                if (prev.prev == null || !b.equals(prev.prev.board)) {
                    minPQ.insert(new Node(b, prev, prev.moves + 1, b.manhattan() + prev.moves + 1));
                }
            }
            for (Board b : twinPrev.board.neighbors()) {
                if (twinPrev.prev == null || !b.equals(twinPrev.prev.board)) {
                    twinMinPQ.insert(new Node(b, twinPrev, twinPrev.moves + 1, b.manhattan() + twinPrev.moves + 1));
                }
            }
            twinPrev = twinMinPQ.delMin();
            prev = minPQ.delMin();
        }
        solutionSteps = new Stack<Board>();
        if (!prev.board.isGoal()) {
            solvable = false;
            return;
        }
        while (prev != null) {
            solutionSteps.push(prev.board);
            prev = prev.prev;
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable())
            return -1;
        return solutionSteps.size() - 1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solutionSteps;
    }

    public static void main(String[] args) {
        // create initial board from file
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = StdIn.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        StdOut.println(solver.moves());
    }
}

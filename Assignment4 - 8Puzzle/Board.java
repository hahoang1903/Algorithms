import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] board;
    private final int size;
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = tiles[i][j];
    }

    public String toString() {
        String ret = size + "\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret += board[i][j] + " ";
            }
            ret += "\n";
        }
        return ret;
    }

    public int dimension() {
        return size;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] != i * size + j + 1 && !(i == size - 1 && j == size - 1))
                    count++;
        return count;
    }

    public int manhattan() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != i * size + j + 1 && board[i][j] != 0) {
                    int goalI = (board[i][j] - 1) / size;
                    int goalJ = (board[i][j] - 1) % size;

                    count += Math.abs(i - goalI) + Math.abs(j - goalJ);
                }
            }
        }
        return count;
    }

    public boolean isGoal() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] != i * size + j + 1 && board[i][j] != 0)
                    return false;
        return true;
    }

    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.size != that.size)
            return false;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (this.board[i][j] != that.board[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    if (i - 1 >= 0) {
                        swap(board, i, j, i - 1, j);
                        q.enqueue(new Board(board));
                        swap(board, i - 1, j, i, j);
                    }
                    if (i + 1 < size) {
                        swap(board, i, j, i + 1, j);
                        q.enqueue(new Board(board));
                        swap(board, i + 1, j, i, j);
                    }
                    if (j - 1 >= 0) {
                        swap(board, i, j, i, j - 1);
                        q.enqueue(new Board(board));
                        swap(board, i, j - 1, i, j);
                    }
                    if (j + 1 < size) {
                        swap(board, i, j, i, j + 1);
                        q.enqueue(new Board(board));
                        swap(board, i, j + 1, i, j);
                    }
                    break;
                }
            }
        }
        return q;
    }

    private void swap(int[][] a, int i1, int j1, int i2, int j2) {
        int tmp = a[i1][j1];
        a[i1][j1] = a[i2][j2];
        a[i2][j2] = tmp;
    }

    public Board twin() {
        int swapI, swapJ;
        Board twin = null;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    if (i == 0) swapI = i + 1;
                    else swapI = i - 1;

                    if (j == 0) swapJ = j + 1;
                    else swapJ = j - 1;

                    swap(board, i, swapJ, swapI, swapJ);
                    twin = new Board(board);
                    swap(board, swapI, swapJ, i, swapJ);
                    break;
                }
            }
        }
        return twin;
    }
}

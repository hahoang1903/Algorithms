import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] grid;
    private final int n;
    private final int size;
    private final WeightedQuickUnionUF uf;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        size = n * n;
        grid = new boolean[n + 1][n + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                grid[i][j] = false;

        uf = new WeightedQuickUnionUF(size + 2);
        for (int i = 0; i < n; i++)
            uf.union(i, size);
        for (int i = size - n; i < size; i++)
            uf.union(i, size + 1);
    }

    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();

        if (!isOpen(row, col)) {
            grid[row][col] = true;

            if (row - 1 >= 1 && grid[row - 1][col])
                uf.union((row - 2) * n + col - 1, (row - 1) * n + col - 1);
            if (row + 1 <= n && grid[row + 1][col])
                uf.union(row * n + col - 1, (row - 1) * n + col - 1);
            if (col - 1 >= 1 && grid[row][col - 1])
                uf.union((row - 1) * n + col - 2, (row - 1) * n + col - 1);
            if (col + 1 <= n && grid[row][col + 1])
                uf.union((row - 1) * n + col, (row - 1) * n + col - 1);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        if (isOpen(row, col))
            if (uf.find((row - 1) * n + col - 1) == uf.find(size))
                return true;

        return false;
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                if (grid[i][j])
                    count++;

        return count;
    }

    public boolean percolates() {
        if (uf.find(size) == uf.find(size + 1))
            return true;
        return false;
    }
}

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] grid;
    private final int n;
    private final int size;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF backwashUF;
    private int openSites;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        size = n * n;
        openSites = 0;
        grid = new boolean[n + 1][n + 1];
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                grid[i][j] = false;

        uf = new WeightedQuickUnionUF(size + 2);
        for (int i = 0; i < n; i++)
            uf.union(i, size);
        for (int i = size - n; i < size; i++)
            uf.union(i, size + 1);

        backwashUF = new WeightedQuickUnionUF(size + 1);
        for (int i = 0; i < n; i++)
            backwashUF.union(i, size);
    }

    public void open(int row, int col) {
        if (isNotInRange(row, col))
            throw new IllegalArgumentException();

        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSites++;

            if (row - 1 >= 1 && isOpen(row - 1, col)) {
                uf.union(map2Dto1D(row - 1, col), map2Dto1D(row, col));
                backwashUF.union(map2Dto1D(row - 1, col), map2Dto1D(row, col));
            }
            if (row + 1 <= n && isOpen(row + 1, col)) {
                uf.union(map2Dto1D(row + 1, col), map2Dto1D(row, col));
                backwashUF.union(map2Dto1D(row + 1, col), map2Dto1D(row, col));
            }
            if (col - 1 >= 1 && isOpen(row, col - 1)) {
                uf.union(map2Dto1D(row, col - 1), map2Dto1D(row, col));
                backwashUF.union(map2Dto1D(row, col - 1), map2Dto1D(row, col));
            }
            if (col + 1 <= n && isOpen(row, col + 1)) {
                uf.union(map2Dto1D(row, col + 1), map2Dto1D(row, col));
                backwashUF.union(map2Dto1D(row, col + 1), map2Dto1D(row, col));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (isNotInRange(row, col))
            throw new IllegalArgumentException();
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (isNotInRange(row, col))
            throw new IllegalArgumentException();

        if (!isOpen(row, col))
            return false;

        if (backwashUF.find((row - 1) * n + col - 1) == backwashUF.find(size))
            return true;

        return false;
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (numberOfOpenSites() == 0)
            return false;
        if (uf.find(size) == uf.find(size + 1))
            return true;
        return false;
    }

    private boolean isNotInRange(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            return true;
        return false;
    }

    private int map2Dto1D(int row, int col) {
        return (row - 1) * n + col - 1;
    }
}

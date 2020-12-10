import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresholds;
    private final double trials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        this.trials = trials;
        thresholds = new double[trials];
        while (trials != 0) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int p = StdRandom.uniform(n) + 1;
                int q = StdRandom.uniform(n) + 1;
                percolation.open(p, q);
            }
            thresholds[trials - 1] = (double) percolation.numberOfOpenSites() / (n * n);
            trials--;
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        double mean = mean();
        double dev = stddev();
        return mean - (CONFIDENCE_95 * dev) / Math.sqrt(trials);
    }

    public double confidenceHi() {
        double mean = mean();
        double dev = stddev();
        return mean + (CONFIDENCE_95 * dev) / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        System.out.println("mean\t\t\t\t\t= " + ps.mean());
        System.out.println("stddev\t\t\t\t\t= " + ps.stddev());
        System.out.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}

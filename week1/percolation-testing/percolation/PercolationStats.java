/******************************************************************************
 *  Name:    Evan Zeimet
 *  NetID:   podnov
 *  Precept: P01
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Description:  Model an n-by-n percolation system using the union-find
 *                data structure.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Dimension should be greater than 0");
        }

        if (trials  <= 0) {
            throw new IllegalArgumentException("Trials should be greater than 0");
        }

        double[] openSiteRatios = runTrials(n, trials);

        this.mean = StdStats.mean(openSiteRatios);
        this.stddev = StdStats.stddev(openSiteRatios);

        double trailsSqrt = Math.sqrt(trials);
        double confidenceVariance = ((1.96 * stddev)  / trailsSqrt);

        this.confidenceLo = mean - confidenceVariance;
        this.confidenceHi = mean + confidenceVariance;
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.confidenceLo;
    }

    public double confidenceHi() {
        return this.confidenceHi;
    }

    private double[] runTrials(int n, int trials) {
        double[] result = new double[trials];

        for (int i = 0; i < trials; i++) {
            double openSiteRatio = runTrial(n);
            result[i] = openSiteRatio;
        }

        return result;
    }

    private double runTrial(int n) {
        int openSiteCount = -1;
        Percolation perc = new Percolation(n);

        int siteCount = n*n;
        int[] siteIndices = createRandomSiteIndicies(siteCount);

        for (int i = 0; i < siteCount; i++) {
            int siteIndex = siteIndices[i];
            int row = (siteIndex / n) + 1;
            int col = (siteIndex % n) + 1;
            perc.open(row, col);

            if (perc.percolates()) {
                openSiteCount = i + 1;
                break;
            }
        }

        return openSiteCount / (double) siteCount;
    }

    private int[] createRandomSiteIndicies(int siteCount) {
        int[] result = new int[siteCount];

        for (int i = 0; i < siteCount; i++) {
            result[i] = i;
        }

        StdRandom.shuffle(result);

        return result;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected exactly 2 arguments");
        }

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, t);

        String message = String.format("mean                    = %f", stats.mean());
        System.out.println(message);

        message = String.format("stddev                  = %f", stats.stddev());
        System.out.println(message);

        message = String.format("%s confidence interval = [%f, %f]",
                "95%",
                stats.confidenceLo(),
                stats.confidenceHi());
        System.out.println(message);
    }

}

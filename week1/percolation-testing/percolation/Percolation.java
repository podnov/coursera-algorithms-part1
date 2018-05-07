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

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int dimension;
    private int openSiteCount;
    private boolean[][] openSites;
    private final WeightedQuickUnionUF uf;
    private final int virtualBottomSiteIndex;
    private final int virtualTopSiteIndex;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Dimension must be greater than 0");
        }

        int siteCount = n * n;

        this.dimension = n;
        this.openSites = new boolean[dimension + 1][dimension + 1];
        this.openSiteCount = 0;
        this.uf = new WeightedQuickUnionUF(siteCount + 2);
        this.virtualTopSiteIndex = createSiteIndex(dimension + 1, 1);
        this.virtualBottomSiteIndex = createSiteIndex(dimension + 1, 2);
    }

    public void open(int row, int col) {
        validateRowCol(row, col);

        if (!openSites[col][row]) {
            openSiteCount++;
            openSites[col][row] = true;

            int siteIndex = createSiteIndex(row, col);

            boolean topRow = row == 1;
            if (topRow) {
                uf.union(siteIndex, virtualTopSiteIndex);
            } else {
                int neighborRow = (row - 1);
                connectOpenNeighbor(siteIndex, neighborRow, col);
            }

            boolean bottomRow = row == dimension;
            if (bottomRow) {
                uf.union(siteIndex, virtualBottomSiteIndex);
            } else {
                int neighborRow = (row + 1);
                connectOpenNeighbor(siteIndex, neighborRow, col);
            }

            boolean hasWestNeighbor = (col > 1);
            if (hasWestNeighbor) {
                /**
                 * <pre>
                 *  |o
                 * n|x o
                 *  |o
                 * </pre>
                 */
                int neighborCol = (col - 1);
                connectOpenNeighbor(siteIndex, row, neighborCol);
            }

            boolean hasEastNeighbor = (col < dimension);
            if (hasEastNeighbor) {
                /**
                 * <pre>
                 *   o|
                 * o x|n
                 *   o|
                 * </pre>
                 */
                int neighborCol = (col + 1);
                connectOpenNeighbor(siteIndex, row, neighborCol);
            }
        }
    }

    private void connectOpenNeighbor(int siteIndex, int neighborRow, int neighborCol) {
        if (isOpen(neighborRow, neighborCol)) {
            int neighborIndex = createSiteIndex(neighborRow, neighborCol);
            uf.union(siteIndex, neighborIndex);
        }
    }

    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        return openSites[col][row];
    }

    public boolean isFull(int row, int col) {
        validateRowCol(row, col);
        int siteIndex = createSiteIndex(row, col);
        return uf.connected(siteIndex, virtualTopSiteIndex);
    }

    public int numberOfOpenSites() {
        return openSiteCount;
    }

    public boolean percolates() {
        return uf.connected(virtualTopSiteIndex, virtualBottomSiteIndex);
    }

    private int createSiteIndex(int row, int col) {
        int base = (row - 1) * dimension;
        return (base + col) - 1;
    }

    private void validateRowCol(int row, int col) {
        if ((row <= 0) || (row > this.dimension)) {
            String message = String.format("Row must be between 1 and %s",
                    dimension);
            throw new IllegalArgumentException(message);
        }
        if ((col <= 0) || (col > this.dimension)) {
            String message = String.format("Columns must be between 1 and %s",
                    dimension);
            throw new IllegalArgumentException(message);
        }
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
        System.out.println(perc.createSiteIndex(1, 1));
        System.out.println(perc.createSiteIndex(5, 5));
        System.out.println(perc.createSiteIndex(10, 10));

        int p = perc.createSiteIndex(1, 1);
        int q = perc.createSiteIndex(1, 2);
        System.out.println(perc.uf.connected(p, q));
        perc.open(1, 1);
        perc.open(1, 2);
        System.out.println(perc.uf.connected(p, q));

        q = perc.createSiteIndex(2, 1);
        System.out.println(perc.uf.connected(p, q));
        perc.open(2, 1);
        System.out.println(perc.uf.connected(p, q));
    }

}

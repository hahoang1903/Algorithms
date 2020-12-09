import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        energy = new int[width()][height()];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++)
                energy[i][j] = -1;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();

        return energy(x, y, false);
    }

    private double energy(int x, int y, boolean forceCalculate) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            energy[x][y] = 1000;
            return 1000;
        }

        if (energy[x][y] != -1 && !forceCalculate)
            return Math.sqrt(energy[x][y]);

        Color colorLeftX = picture.get(x + 1, y);
        Color colorRightX = picture.get(x - 1, y);
        int xRedDiff = colorLeftX.getRed() - colorRightX.getRed();
        int xGreenDiff = colorLeftX.getGreen() - colorRightX.getGreen();
        int xBlueDiff = colorLeftX.getBlue() - colorRightX.getBlue();
        int xGraSquare = xRedDiff * xRedDiff + xGreenDiff * xGreenDiff + xBlueDiff * xBlueDiff;

        Color colorTopY = picture.get(x, y - 1);
        Color colorBottomY = picture.get(x, y + 1);
        int yRedDiff = colorBottomY.getRed() - colorTopY.getRed();
        int yGreenDiff = colorBottomY.getGreen() - colorTopY.getGreen();
        int yBlueDiff = colorBottomY.getBlue() - colorTopY.getBlue();
        int yGraSquare = yRedDiff * yRedDiff + yGreenDiff * yGreenDiff + yBlueDiff * yBlueDiff;

        energy[x][y] = xGraSquare + yGraSquare;
        return Math.sqrt(xGraSquare + yGraSquare);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width()][height()];
        int[][] vertexTo = new int[width()][height()];

        for (int x = 0; x < width(); x++)
            for (int y = 0; y < height(); y++)
                distTo[x][y] = Double.POSITIVE_INFINITY;

        for (int x = 0; x < width(); x++)
            distTo[x][0] = energy(x, 0);

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                if (x > 0)
                    relax(distTo, vertexTo, x, y, x - 1, y + 1);
                if (x < width() - 1)
                    relax(distTo, vertexTo, x, y, x + 1, y + 1);
                relax(distTo, vertexTo, x, y, x, y + 1);
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int minX = -1;
        for (int x = 0; x < width(); x++) {
            if (distTo[x][height() - 1] < minEnergy) {
                minEnergy = distTo[x][height() - 1];
                minX = x;
            }
        }

        int[] seam = new int[height()];
        int prevX = -1;
        for (int y = height() - 1; y >= 0; y--) {
            seam[y] = prevX == -1 ? minX : prevX;
            prevX = vertexTo[prevX == -1 ? minX : prevX][y];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (isInvalidSeam(height(), width(), seam))
            throw new IllegalArgumentException();

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (isInvalidSeam(width(), height(), seam))
            throw new IllegalArgumentException();

        Picture carved = new Picture(width() - 1, height());
        int[][] newEnergy = new int[width() - 1][height()];

        for (int y = 0; y < carved.height(); y++) {
            for (int x = 0; x < seam[y]; x++) {
                carved.set(x, y, picture.get(x, y));
                newEnergy[x][y] = energy[x][y];
            }
            for (int x = seam[y]; x < carved.width(); x++) {
                carved.set(x, y, picture.get(x + 1, y));
                newEnergy[x][y] = energy[x + 1][y];
            }
        }

        picture = carved;
        energy = newEnergy;

        for (int y = 0; y < height(); y++) {
            energy(Math.max(seam[y] - 1, 0), y, true);
            energy(Math.min(seam[y], width() - 1), y, true);
        }
    }

    private void transpose() {
        Picture transposed = new Picture(height(), width());

        for (int i = 0; i < transposed.width(); i++)
            for (int j = 0; j < transposed.height(); j++)
                transposed.set(i, j, picture.get(j, i));

        int[][] transposedEnergy = new int[height()][width()];
        for (int i = 0; i < transposed.width(); i++)
            for (int j = 0; j < transposed.height(); j++)
                transposedEnergy[i][j] = energy[j][i];

        energy = transposedEnergy;
        picture = transposed;
    }

    private void relax(double[][] distTo, int[][] vertexTo, int x1, int y1, int x2, int y2) {
        if (distTo[x2][y2] > distTo[x1][y1] + energy(x2, y2)) {
            distTo[x2][y2] = distTo[x1][y1] + energy(x2, y2);
            vertexTo[x2][y2] = x1;
        }
    }

    private boolean isInvalidSeam(int size1, int size2, int[] seam) {
        if (size1 <= 1)
            return true;
        if (seam == null)
            return true;
        if (seam.length != size2)
            return true;
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= size1)
                return true;
            if (Math.abs(seam[i] - seam[Math.min(i + 1, seam.length - 1)]) > 1)
                return true;
        }

        return false;
    }
}
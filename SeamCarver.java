/*
Contains an alogirthm for resizing images in a content aware manner using seam carving 
*/

import java.util.PriorityQueue;

public class SeamCarver {
    // create integer variables for the width and height of the image
    private int width;
    private int height;
    //create a Picture variable for the image
    private Picture pictureCopy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("argument to SeamCarver() is null\n");
        }
        pictureCopy = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(pictureCopy);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateColumnIndex(x);
        validateRowIndex(y);

        // border pixels
        if (x == 0 || x == width -1 || y == 0 || y == height -1) {
            return 1000;
        }

        int up, down, left, right;
        up = pictureCopy.getRGB(x, y - 1);
        down = pictureCopy.getRGB(x, y + 1);
        left = pictureCopy.getRGB(x - 1, y);
        right = pictureCopy.getRGB(x + 1, y);
        double gradientY = gradient(up, down);
        double gradientX = gradient(left, right);

        return Math.sqrt(gradientX + gradientY);
    }

    private double gradient(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >>  8) & 0xFF;
        int b1 = (rgb1 >>  0) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2 = (rgb2 >>  0) & 0xFF;

        return Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2)
                + Math.pow(b1 - b2, 2);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[col][row] = energy(col, row);
            }
        }

        // Finding the vertical seam using dynamic programming
        int[] vSeam = new int[height];
        double[][] summedEnergies = new double[width][height];
        int[][] paths = new int[width][height];

        // Initialize all values in the summedEnergies array to the maximum integer value
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                summedEnergies[col][row] = Integer.MAX_VALUE;
                if (row == 0) {
                    summedEnergies[col][row] = energy(col, row);
                }
            }
        }

        // Begin calculating the actual value for summedEnergies at each pixel in the image
        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                // For a valid vertical seam, can only move down, down and to the right, or down and to the left from the current pixel
                // Once values are updated, the path is recorded in the paths array
                if (col - 1 >= 0 && energy[col - 1][row + 1] + summedEnergies[col][row] < summedEnergies[col - 1][row + 1]) {
                    summedEnergies[col - 1][row + 1] = energy[col - 1][row + 1] + summedEnergies[col][row];
                    paths[col - 1][row + 1] = col;
                }
                if (energy[col][row + 1] + summedEnergies[col][row] < summedEnergies[col][row + 1]) {
                    summedEnergies[col][row + 1] = energy[col][row + 1] + summedEnergies[col][row];
                    paths[col][row + 1] = col;
                }
                if (col + 1 < width && energy[col + 1][row + 1] + summedEnergies[col][row] < summedEnergies[col + 1][row + 1]) {
                    summedEnergies[col + 1][row + 1] = energy[col + 1][row + 1] + summedEnergies[col][row];
                    paths[col + 1][row + 1] = col;
                }
            }
        }

        // For a vertical seam, we will start in the first row. Find the column index of the pixel in the first row with the lowest energy value.
        double min = summedEnergies[0][height - 1];
        int minCol = 0;
        for (int col = 1; col < width; col++) {
            if (min > summedEnergies[col][height - 1]) {
                min = summedEnergies[col][height - 1];
                minCol = col;
            }
        }
        
        int row = height - 1;
        int col = paths[minCol][row];

        // Loop back through paths array and add column indexes to the vSeam array 
        while (row > 0) {
            vSeam[row] = col;
            row--;
            col = paths[col][row];
        }

        return vSeam;
    }

    // The logic for finding the horizontal seam is the same as the vertical seam just in a different direction.
    public int[] findHorizontalSeam() {
        double[][] energy = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[col][row] = energy(col, row);
            }
        }

        int[] hSeam = new int[width];
        
        double[][] summedEnergies = new double[width][height];
        int[][] paths = new int[width][height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                summedEnergies[col][row] = Integer.MAX_VALUE;
                if (col == 0) {
                    summedEnergies[col][row] = energy(col, row);
                }
            }
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {

                if (row - 1 >= 0 && energy[col + 1][row - 1] + summedEnergies[col][row] < summedEnergies[col + 1][row - 1]) {
                    summedEnergies[col + 1][row - 1] = energy[col + 1][row - 1] + summedEnergies[col][row];
                    paths[col + 1][row - 1] = row;
                }
                if (energy[col + 1][row] + summedEnergies[col][row] < summedEnergies[col + 1][row]) {
                    summedEnergies[col + 1][row] = energy[col + 1][row] + summedEnergies[col][row];
                    paths[col + 1][row] = row;
                }
                if (row + 1 < height && energy[col + 1][row + 1] + summedEnergies[col][row] < summedEnergies[col + 1][row + 1]) {
                    summedEnergies[col + 1][row + 1] = energy[col + 1][row + 1] + summedEnergies[col][row];
                    paths[col + 1][row + 1] = row;
                }
            }
        }

        double min = summedEnergies[width - 1][0];
        int minRow = 0;
        for (int row = 1; row < height; row++) {
            if (min > summedEnergies[width- 1][row]) {
                min = summedEnergies[width - 1][row];
                minRow = row;
            }
        }

        int col = width - 1;
        int row = paths[col][minRow];

    
        while (col > 0) {
            hSeam[col] = row;
            col--;
            row = paths[col][row];
        }

        return hSeam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("the argument to removeVerticalSeam() is null\n");
        }
        // If the seam does not go all the way across the picture, throw an exception
        if (seam.length != height) {
            throw new IllegalArgumentException("the length of seam not equal height\n");
        }
        validateSeam(seam);
        if (width <= 1) {
            throw new IllegalArgumentException("the width of the picture is less than or equal to 1\n");
        }

        // Create a new picture object that has the width of the current picture minus 1 to account for the vertical seam to be removed
        Picture tmpPicture = new Picture(width - 1, height);

        // Loop through the current picture and remove the vertical seam by replacing each pixel in the vertical seam with the pixel directly to the right of it
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                validateColumnIndex(seam[row]);
                if (col < seam[row]) {
                    tmpPicture.setRGB(col, row, pictureCopy.getRGB(col, row));
                } else {
                    tmpPicture.setRGB(col, row, pictureCopy.getRGB(col + 1, row));
                }
            }
        }
        
        pictureCopy = tmpPicture;
        width--;
    }

    // Follows the same logic as removing the vertical seam, just in a different direction
    public void removeHorizontalSeam(int[] seam) {
        //TODO: Remove a horizontal seam
        if (seam == null) {
            throw new IllegalArgumentException("the argument to removeVerticalSeam() is null\n");
        }
        if (seam.length != width) {
            throw new IllegalArgumentException("the length of seam not equal height\n");
        }
        validateSeam(seam);
        if (height <= 1) {
            throw new IllegalArgumentException("the width of the picture is less than or equal to 1\n");
        }

        Picture tmpPicture = new Picture(width, height - 1);
        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                validateRowIndex(seam[col]);
                if (row < seam[col]) {
                    tmpPicture.setRGB(col, row, pictureCopy.getRGB(col, row));
                } else {
                    tmpPicture.setRGB(col, row, pictureCopy.getRGB(col, row + 1));
                }
            }
        }
        pictureCopy = tmpPicture;
        height--;
    }

    // transpose the current pictureCopy
    private void transpose() {
        Picture tmpPicture = new Picture(height, width);
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                tmpPicture.setRGB(col, row, pictureCopy.getRGB(row, col));
            }
        }
        pictureCopy = tmpPicture;
        int tmp = height;
        height = width;
        width = tmp;
    }

    // make sure column index is bewteen 0 and width - 1
    private void validateColumnIndex(int col) {
        if (col < 0 || col > width -1) {
            throw new IllegalArgumentException("colmun index is outside its prescribed range\n");
        }
    }

    // make sure row index is between 0 and height - 1
    private void validateRowIndex(int row) {
        if (row < 0 || row > height -1) {
            throw new IllegalArgumentException("row index is outside its prescribed range\n");
        }
    }

    // make sure two adjacent entries differ within 1
    private void validateSeam(int[] seam) {
        for (int i = 1; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("two adjacent entries differ by more than 1 in seam\n");
            }
        }
    }
}

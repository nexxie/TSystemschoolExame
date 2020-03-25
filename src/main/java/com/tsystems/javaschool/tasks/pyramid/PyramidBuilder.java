package com.tsystems.javaschool.tasks.pyramid;

import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        try {
            if (inputNumbers.contains(null)) throw new CannotBuildPyramidException();
            Collections.sort(inputNumbers);
        } catch (UnsupportedOperationException | OutOfMemoryError e) {
            throw new CannotBuildPyramidException();
        }

        int total = 0;
        int height = 0;
        while (total != inputNumbers.size()) {
            height++;
            total += height;

            if (total > inputNumbers.size()) throw new CannotBuildPyramidException();
        }

        int width = 2 * height - 1;

        int[][] temp1 = new int[height][width];
        int arrIndex = inputNumbers.size() - 1;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == j) {
                    int k = j;
                    while (k < width - j) {
                        temp1[i][k] = inputNumbers.get(arrIndex--);
                        if (arrIndex <= 0) break;
                        k += 2;
                    }
                }
            }
        }

        int[][] result = new int[temp1.length][temp1[0].length];
        for (int row = 0; row < temp1.length; row++) {
            result[row] = temp1[temp1.length - row - 1];
        }

        for (int j = 0; j < result.length; j++) {
            for (int i = 0; i < result[j].length / 2; i++) {
                int temp2 = result[j][i];
                result[j][i] = result[j][result[j].length - i - 1];
                result[j][result[j].length - i - 1] = temp2;
            }
        }

        return result;
    }

}



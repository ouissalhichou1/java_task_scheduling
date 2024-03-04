package Ressources;

import java.io.Serializable;

public class DataMedian implements Serializable {

    public double density;
    public int[][] image;
    public double[][] kernel; // Added kernel matrix

    public DataMedian(int[][] image, double density) {
        this.image = image;
        this.density = density;
        this.kernel = createKernelMatrix(density); // Create kernel matrix
    }

    public int[][] applyMedianFilter() {
        int width = image.length;
        int height = image[0].length;
        int[][] output = new int[width][height];

        // Calculate window size based on kernel dimensions
        // Calculate window size based on kernel dimensions
        int kernelSize = kernel.length;
        int windowSize = kernelSize; // For the median filter, you usually consider the same-sized neighborhood as the kernel


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Extract the neighboring pixel values
                int[] neighborValues = new int[windowSize * windowSize];
                int index = 0;
                for (int dy = -windowSize / 2; dy <= windowSize / 2; dy++) {
                    for (int dx = -windowSize / 2; dx <= windowSize / 2; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        // Check bounds
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            neighborValues[index++] = image[nx][ny];
                        }
                    }
                }

                // Sort the neighboring pixel values
                java.util.Arrays.sort(neighborValues);

                // Get the median value
                // Get the median value
                int medianValue = 0;  // Default value in case no valid neighbors are found
                if (index > 0) {
                    int medianIndex = index / 2;
                    medianValue = neighborValues[medianIndex];
                }

                // Assign the median value to the output pixel
                output[x][y] = medianValue;

            }
        }

        return output;
    }

    private double[][] createKernelMatrix(double density) {
        // Assuming a 3x3 kernel for simplicity
        int kernelSize = 3;
        double[][] kernel = new double[kernelSize][kernelSize];
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                kernel[i][j] = density;
            }
        }
        return kernel;
    }
}

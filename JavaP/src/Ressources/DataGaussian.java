package Ressources;

import java.io.Serializable;

public class DataGaussian implements Serializable {

    public int[][] image;
    public double density;
    public double[][] kernel; // Added kernel matrix

    public DataGaussian(int[][] image, double density) {
        this.image = image;
        this.density = density;
        this.kernel = createKernelMatrix(density); // Create kernel matrix
    }

    public static int[][] applyGaussianFilter(int[][] pixels, double[][] kernel) {
        int width = pixels.length;
        int height = pixels[0].length;
        int[][] output = new int[width][height];

        // Apply the filter to each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double sum = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int nx = x + dx;
                        int ny = y + dy;
                        // Check bounds
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            // Apply the kernel to the pixel and its neighbors
                            sum += pixels[nx][ny] * kernel[dx + 1][dy + 1];
                        }
                    }
                }
                // Set the output pixel value
                output[x][y] = (int) Math.round(sum);
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

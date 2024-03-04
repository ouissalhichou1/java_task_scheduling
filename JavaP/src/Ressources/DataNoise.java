package Ressources;

import java.io.Serializable;
import java.util.Random;

public class DataNoise implements Serializable {
    public int[][] image;
    public double density;
    public double[][] kernel; // Added kernel matrix

    public DataNoise(int[][] image, double density) {
        this.image = image;
        this.density = density;
        this.kernel = createKernelMatrix(density); // Create kernel matrix
    }

    // Method to create a kernel matrix from the density value
    private double[][] createKernelMatrix(double density) {
        // Assuming a 3x3 kernel for simplicity
        double[][] kernel = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                kernel[i][j] = density;
            }
        }
        return kernel;
    }

    public static int[][] addNoise(int[][] image, double[][] kernel) {
        int height = image.length;
        int width = image[0].length;
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image[i][j];
                if (random.nextDouble() < kernel[1][1] / 2) {
                    pixel = 0xffffffff;
                } else if (random.nextDouble() < kernel[1][1] / 2) {
                    pixel = 0xff000000;
                }
                image[i][j] = pixel;
            }
        }
        return image;
    }
}

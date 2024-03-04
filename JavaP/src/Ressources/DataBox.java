package Ressources;

import java.io.Serializable;

public class DataBox implements Serializable {
    public double density;
    public int[][] image;
    public double[][] kernel; // Added kernel matrix

    public DataBox(int[][] image, double density) {
        this.image = image;
        this.density = density;
        this.kernel = createKernelMatrix(density); // Create kernel matrix
    }

    // Method to create a kernel matrix from the density value
    private double[][] createKernelMatrix(double density) {
        // Assuming a 3x3 kernel for simplicity
        int kernelSize = (int) Math.ceil(density * 3); // Adjust 3 based on the desired default kernel size
        double[][] kernel = new double[kernelSize][kernelSize];
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                kernel[i][j] = density;
            }
        }
        return kernel;
    }

    public static int[][] applyBoxBlurFilter(int[][] pixels, double[][] kernel) {
        int width = pixels.length;
        int height = pixels[0].length;
        int[][] output = new int[width][height];

        int kernelSize = kernel.length;
        int kernelRadius = kernelSize / 2;

        // Applying Box Blur filter logic
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;
                int numPixels = 0;

                // Iterate over the pixels in the kernel window
                for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
                    for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
                        // Calculate the coordinates of the pixel in the original image
                        int px = Math.min(Math.max(x + kx, 0), width - 1);
                        int py = Math.min(Math.max(y + ky, 0), height - 1);

                        // Get the color components of the pixel
                        int color = pixels[px][py];
                        int red = (color >> 16) & 0xFF;
                        int green = (color >> 8) & 0xFF;
                        int blue = color & 0xFF;

                        // Accumulate the color values
                        sumRed += red;
                        sumGreen += green;
                        sumBlue += blue;
                        numPixels++;
                    }
                }

                // Calculate the average color values
                int avgRed = sumRed / numPixels;
                int avgGreen = sumGreen / numPixels;
                int avgBlue = sumBlue / numPixels;

                // Combine the RGB components back into a single pixel color
                output[x][y] = (avgRed << 16) | (avgGreen << 8) | avgBlue;
            }
        }

        return output;
    }
}

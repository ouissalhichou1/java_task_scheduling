package Ressources;

import java.io.Serializable;

public class Databright implements Serializable {
    public int[][] image;
    public double density;
    public double[][] kernel; // Added kernel matrix

    public Databright(int[][] image, double density) {
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

    public static int[][] brightnessFilter(int[][] pixels, double[][] kernel) {
        int width = pixels.length;
        int height = pixels[0].length;
        int[][] output = new int[width][height];

        int kernelSize = kernel.length;
        int kernelRadius = kernelSize / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the current pixel color
                int color = pixels[x][y];

                // Extract the RGB components
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // Apply the kernel to each pixel
                double newRed = 0;
                double newGreen = 0;
                double newBlue = 0;
                for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
                    for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
                        int pixelX = Math.min(Math.max(x + kx, 0), width - 1);
                        int pixelY = Math.min(Math.max(y + ky, 0), height - 1);

                        // Apply kernel value to pixel
                        double weight = kernel[ky + kernelRadius][kx + kernelRadius];
                        newRed += ((pixels[pixelX][pixelY] >> 16) & 0xFF) * weight;
                        newGreen += ((pixels[pixelX][pixelY] >> 8) & 0xFF) * weight;
                        newBlue += (pixels[pixelX][pixelY] & 0xFF) * weight;
                    }
                }

                // Normalize and update the pixel color
                red = Math.min(255, Math.max(0, (int) newRed));
                green = Math.min(255, Math.max(0, (int) newGreen));
                blue = Math.min(255, Math.max(0, (int) newBlue));

                // Combine the RGB components back into a single pixel color
                output[x][y] = (red << 16) | (green << 8) | blue;
            }
        }

        return output;
    }
}

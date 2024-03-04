package Ressources;

import java.io.Serializable;

public class DataConte implements Serializable {
    public int[][] image;

    public DataConte(int[][] image) {
        this.image = image;
    }

    public static int[][] applyConteFilter(int[][] pixels) {
        int width = pixels.length;
        int height = pixels[0].length;
        int[][] output = new int[width][height];

        // Applying Conte filter logic
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the current pixel color
                int color = pixels[x][y];

                // Extract the RGB components
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // Apply some transformations to the RGB values to achieve the Conte effect
                // For simplicity, I'll just invert the RGB values
                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;

                // Combine the RGB components back into a single pixel color
                output[x][y] = (red << 16) | (green << 8) | blue;
            }
        }

        return output;
    }
}

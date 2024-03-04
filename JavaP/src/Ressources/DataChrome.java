package Ressources;

import java.io.Serializable;

public class DataChrome implements Serializable {
    public int[][] image;

    public DataChrome(int[][] image) {
        this.image = image;
    }

    public static int[][] applyChromeFilter(int[][] pixels) {
        int width = pixels.length;
        int height = pixels[0].length;
        int[][] output = new int[width][height];

        // Applying Chrome filter logic
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the current pixel color
                int color = pixels[x][y];

                // Extract the RGB components
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // Apply transformations to the RGB values to achieve the Chrome effect
                // For example, you can boost the red and blue channels while reducing the green channel
                // This will give a typical "Chrome" effect with vivid colors
                // For simplicity, let's increase the red and blue channels and decrease the green channel
                red = Math.min(255, (int) (1.2 * red));
                blue = Math.min(255, (int) (1.2 * blue));
                green = (int) (0.8 * green);

                // Clip the values to the range of 0-255
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));

                // Combine the RGB components back into a single pixel color
                output[x][y] = (red << 16) | (green << 8) | blue;
            }
        }

        return output;
    }
}


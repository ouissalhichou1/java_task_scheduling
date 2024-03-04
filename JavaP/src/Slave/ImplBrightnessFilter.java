package Slave;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import MainServer.ImageDivider.SubMatrix;
import RessourcesForRMI.BrightnessFilter;

/**
 * ImplFilters
 */
public class ImplBrightnessFilter extends UnicastRemoteObject implements BrightnessFilter {

    public ImplBrightnessFilter() throws RemoteException {
        super();
    }

    // convolution--------------------------------------------------
    @Override
    public SubMatrix applyBrightness(SubMatrix inputSubMatrix, double brightness) {
        int[][] pixels = inputSubMatrix.matrix;
        int width = pixels.length;
        int height = pixels[0].length;

        // Adjust the brightness of each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = pixels[x][y];
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // Adjust the intensity of each color component based on the brightness value
                red = (int) Math.min(255, Math.max(0, red * brightness));
                green = (int) Math.min(255, Math.max(0, green * brightness));
                blue = (int) Math.min(255, Math.max(0, blue * brightness));

                // Update the pixel color
                pixels[x][y] = (red << 16) | (green << 8) | blue;
            }
        }

        return inputSubMatrix;
    }



}
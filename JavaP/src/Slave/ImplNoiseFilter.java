package Slave;

import MainServer.ImageDivider.SubMatrix;
import RessourcesForRMI.NoiseFilter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ImplNoiseFilter
 */
public class ImplNoiseFilter extends UnicastRemoteObject implements NoiseFilter {

    public ImplNoiseFilter() throws RemoteException {
        super();
    }

    // Add noise--------------------------------------------------
    @Override
    public SubMatrix applyNoise(SubMatrix inputSubMatrix, double noiseLevel) {
        int[][] pixels = inputSubMatrix.matrix;
        int width = pixels.length;
        int height = pixels[0].length;

        // Add noise to each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[x][y];

                // Add noise based on the noise level
                if (Math.random() < noiseLevel / 2) {
                    pixel = 0xffffffff; // White color
                } else if (Math.random() < noiseLevel / 2) {
                    pixel = 0xff000000; // Black color
                }

                pixels[x][y] = pixel;
            }
        }

        return inputSubMatrix;
    }
}

package Slave;

import MainServer.ImageDivider.SubMatrix;
import Ressources.DataMedian;
import RessourcesForRMI.MedianFilter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ImplMedianFilter
 */
public class ImplMedianFilter extends UnicastRemoteObject implements MedianFilter {

    public ImplMedianFilter() throws RemoteException {
        super();
    }

    // Median Filter--------------------------------------------------
    @Override
    public SubMatrix applyMedian(SubMatrix inputSubMatrix, double density) {
        int[][] pixels = inputSubMatrix.matrix;
        int width = pixels.length;
        int height = pixels[0].length;

        // Create DataMedian object to encapsulate image and density
        DataMedian dataMedian = new DataMedian(pixels, density);

        // Apply Median filter using the instance method
        int[][] resultPixels = dataMedian.applyMedianFilter();

        // Update the inputSubMatrix with the result
        inputSubMatrix.matrix = resultPixels;

        return inputSubMatrix;
    }
}

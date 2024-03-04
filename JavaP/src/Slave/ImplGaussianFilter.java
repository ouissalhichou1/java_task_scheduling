package Slave;

import MainServer.ImageDivider.SubMatrix;
import Ressources.DataGaussian;
import RessourcesForRMI.GaussianFilter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ImplGaussianFilter
 */
public class ImplGaussianFilter extends UnicastRemoteObject implements GaussianFilter {

    public ImplGaussianFilter() throws RemoteException {
        super();
    }

    // Gaussian Filter--------------------------------------------------
    @Override
    public SubMatrix applyGaussian(SubMatrix inputSubMatrix, double density) {
        int[][] pixels = inputSubMatrix.matrix;
        int width = pixels.length;
        int height = pixels[0].length;

        // Create DataGaussian object to encapsulate image and density
        DataGaussian dataGaussian = new DataGaussian(pixels, density);

        // Apply Gaussian filter
        int[][] resultPixels = DataGaussian.applyGaussianFilter(dataGaussian.image, dataGaussian.kernel);

        // Update the inputSubMatrix with the result
        inputSubMatrix.matrix = resultPixels;

        return inputSubMatrix;
    }
}

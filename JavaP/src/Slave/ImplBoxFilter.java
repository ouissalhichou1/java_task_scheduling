package Slave;

import MainServer.ImageDivider.SubMatrix;
import Ressources.DataBox;
import RessourcesForRMI.BoxFilter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ImplBoxFilter
 */
public class ImplBoxFilter extends UnicastRemoteObject implements BoxFilter {

    public ImplBoxFilter() throws RemoteException {
        super();
    }

    // Box Blur Filter--------------------------------------------------
    @Override
    public SubMatrix applyBox(SubMatrix inputSubMatrix, double density) {
        int[][] pixels = inputSubMatrix.matrix;
        int width = pixels.length;
        int height = pixels[0].length;

        // Create DataBox object to encapsulate image and density
        DataBox dataBox = new DataBox(pixels, density);

        // Apply Box Blur filter
        int[][] resultPixels = DataBox.applyBoxBlurFilter(dataBox.image, dataBox.kernel);

        // Update the inputSubMatrix with the result
        inputSubMatrix.matrix = resultPixels;

        return inputSubMatrix;
    }
}

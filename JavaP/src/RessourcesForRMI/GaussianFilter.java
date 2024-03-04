package RessourcesForRMI;

import MainServer.ImageDivider.SubMatrix;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Filters
 */

public interface GaussianFilter extends Remote {
    SubMatrix applyGaussian(SubMatrix inputSubMatrix, double Gaussian) throws RemoteException;
}

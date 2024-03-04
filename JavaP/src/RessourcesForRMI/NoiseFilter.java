package RessourcesForRMI;

import MainServer.ImageDivider.SubMatrix;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Filters
 */

public interface NoiseFilter extends Remote {
    SubMatrix applyNoise(SubMatrix inputSubMatrix, double brightness) throws RemoteException;
}

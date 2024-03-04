package RessourcesForRMI;

import MainServer.ImageDivider.SubMatrix;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Filters
 */

public interface MedianFilter extends Remote {
    SubMatrix applyMedian(SubMatrix inputSubMatrix, double Median) throws RemoteException;
}

package RessourcesForRMI;

import MainServer.ImageDivider.SubMatrix;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Filters
 */

public interface BoxFilter extends Remote {
    SubMatrix applyBox(SubMatrix inputSubMatrix, double Box) throws RemoteException;
}

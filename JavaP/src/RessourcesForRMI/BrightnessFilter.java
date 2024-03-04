package RessourcesForRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

import MainServer.ImageDivider.SubMatrix;
/**
 * Filters
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrightnessFilter extends Remote {
    SubMatrix applyBrightness(SubMatrix inputSubMatrix, double brightness) throws RemoteException;
}

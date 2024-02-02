// Filters.java
package RessourcesForRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import MainServer.SubMatrix;

public interface Filters extends Remote {
    SubMatrix applyFilter(SubMatrix inputSubMatrix, int[][] ker) throws RemoteException;
}

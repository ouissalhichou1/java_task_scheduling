package RessourcesForRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Filters
 */
public interface Filters extends Remote {

    public MainServer.Worker.SubMatrix applyFilter(MainServer.Worker.SubMatrix inputMatrix, int[][] ker) throws RemoteException;

}
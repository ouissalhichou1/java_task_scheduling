package Slave;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SlaveInterface extends Remote {
    ArrayList<Integer> performOperation(ArrayList<Integer> data) throws RemoteException;
}

package slave;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Slave extends UnicastRemoteObject implements SlaveInterface {
    protected Slave() throws RemoteException {
        super();
    }

    public ArrayList<Integer> performOperation(ArrayList<Integer> data) {
        // Perform some operation on the data and return the result
        return data;
    }
}

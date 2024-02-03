package RessourcesForRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class SlaveDataList extends UnicastRemoteObject {
    private static final long serialVersionUID = 1L;

    public static ArrayList<WorkerData> ListWorkers = new ArrayList<>();

    public SlaveDataList() throws RemoteException {
        super();
    }

    public void addWorker(String linkRMI) {
        ListWorkers.add(new WorkerData(linkRMI));
    }

    public ArrayList<WorkerData> DispoWorkers() {
        ArrayList<WorkerData> tmpWokers = new ArrayList<>();
        for (WorkerData tmpWorker : ListWorkers) {
            if (tmpWorker.dispo) {
                tmpWokers.add(tmpWorker);
            }
        }
        return tmpWokers;
    }
}

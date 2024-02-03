package RessourcesForRMI;

import java.io.Serializable;

public class WorkerData implements Serializable {
    private static final long serialVersionUID = 1L;

    public String linkRMI;
    public boolean dispo;

    public WorkerData(String linkRMI, boolean dispo) {
        this.linkRMI = linkRMI;
        this.dispo = dispo;
    }

    public WorkerData(String linkRMI) {
        this.linkRMI = linkRMI;
        this.dispo = true; // Assuming true means available
    }
}

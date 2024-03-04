package RessourcesForRMI;

public class SlaveData {
    public String linkRMI;
    public int dispo;

    public SlaveData(String linkRMI, int dispo){
        this.linkRMI=linkRMI;
        this.dispo=dispo;
    }

    public SlaveData(String linkRMI){
        this.linkRMI=linkRMI;
        this.dispo=1;
    }
}
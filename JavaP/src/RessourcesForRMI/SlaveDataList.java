package RessourcesForRMI;

import java.util.ArrayList;

public class SlaveDataList {
    public static ArrayList<SlaveData> ListSlaves = new ArrayList<SlaveData>();

    public static void addSlave(String linkRMI){
        ListSlaves.add(new SlaveData(linkRMI, 1)); // Set dispo to 1 indicating the slave is available
    }

    public static ArrayList<SlaveData> DispoSlaves(){
        ArrayList<SlaveData> tmpSlaves=new ArrayList<SlaveData>();
        for (SlaveData tmpSlave : ListSlaves) {
            if(tmpSlave.dispo == 1){
                tmpSlaves.add(tmpSlave);
            }
        }
        return tmpSlaves;
    }
}

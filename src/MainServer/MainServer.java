package MainServer;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import RessourcesForRMI.SlaveDataList;

public class MainServer extends Thread {
    private static Executor executor;
    private static TaskQueue taskQueue;
    private static int TaskID = 0;
    static int MainServer_port;
    static String MainServer_host;

    private static SlaveDataList slaveDataList; // Create an instance

    static {
        try {
            slaveDataList = new SlaveDataList();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        /*
         * Read Properties from file
         */
        Properties prop = new Properties();
        FileInputStream ip;
        // Default
        String FileConfiguration = "C:\\Users\\hp\\Documents\\GitHub\\java_task_scheduling\\cfgMainServer.properties";
        if (args.length > 0)
            FileConfiguration = args[0];
        try {
            ip = new FileInputStream(FileConfiguration);
            prop.load(ip);
        } catch (Exception e2) {
            System.err.println("Error loading properties file: " + e2.getMessage());
            e2.printStackTrace();
            System.exit(1);
        }

        /*
         * Run the socket of Worker
         */
        MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
        MainServer_host = prop.getProperty("MainServer.host");

        /*
         * GET SERVERS
         */
        getSlaves(prop);
        System.out.println("Number of Slaves: " + slaveDataList.ListWorkers.size());

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(MainServer_port);
            ss.setReuseAddress(true); // Set socket options before binding

            executor = Executors.newFixedThreadPool(10);
            taskQueue = new TaskQueue();

            System.out.println("Server started at port " + MainServer_port);

            // Launch Mini Workers Threads executing the Queue
            for (int i = 0; i < 7; i++) {
                executor.execute(new ServerThread(taskQueue));
            }

            // Listen to clients
            while (true) {
                // Accept the socket from the client
                Socket soc = ss.accept();
                System.out.println("New Task in queue: " + TaskID);
                Task newTask = new Task(soc, TaskID++);
                taskQueue.add(newTask);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getSlaves(Properties prop) {
        String hostSlv = prop.getProperty("Worker1.host");
        String portSlv = prop.getProperty("Worker1.port");
        int i = 1;
        while (hostSlv != null) {
            slaveDataList.addWorker("rmi://" + hostSlv + ":" + portSlv + "/Worker");
            i++;

            hostSlv = prop.getProperty("Worker" + i + ".host");
            portSlv = prop.getProperty("Worker" + i + ".port");
        }
        return i - 1;
    }
}

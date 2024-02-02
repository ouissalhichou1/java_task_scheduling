package MainServer;

import MainServer.ImplFilters;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Worker {
    // TODO
    /*
     * Listen to MainServer
     * Send OK
     * Recieve Task + Data
     * (The data depends on the type of task)
     * Execute Task
     * Send Result to MainServer
     *
     *
     */

    static int Worker_port;
    static String Worker_host;
    public static void main(String[] args) throws UnknownHostException {

        Worker_port =Integer.parseInt(args[0]);

        InetAddress localHost = InetAddress.getLocalHost();
            // Get the IP address of the local host
            String ipAddress = localHost.getHostAddress();
            // Print the IP address of the local host
            System.out.println("Worker is running at  " + ipAddress +":"+ Worker_port+"/Worker" );

            Worker_host = ipAddress;

        try {
            LocateRegistry.createRegistry(Worker_port);
            ImplFilters ob = new ImplFilters();

            Naming.rebind("rmi://"+Worker_host+":"+Worker_port+"/Worker", ob);

            System.out.println("server ready.........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

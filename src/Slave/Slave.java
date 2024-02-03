package Slave; // Update the package statement if necessary

import MainServer.ImplFilters;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Slave {
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

    static int Slave_port;
    static String Slave_host;
    public static void main(String[] args) throws UnknownHostException {
        if (args.length == 0) {
            System.out.println("Usage: java Slave.Slave <port>");
            System.exit(1);
        }

        Slave_port = Integer.parseInt(args[0]);

        InetAddress localHost = InetAddress.getLocalHost();
        // Get the IP address of the local host
        String ipAddress = localHost.getHostAddress();
        // Print the IP address of the local host
        System.out.println("Slave is running at  " + ipAddress + ":" + Slave_port + "/Slave" );

        Slave_host = ipAddress;

        /*
         * Run the socket of Slave
         */

        try {
            LocateRegistry.createRegistry(Slave_port);
            ImplFilters ob = new ImplFilters();

            Naming.rebind("rmi://" + Slave_host + ":" + Slave_port + "/Slave", ob);

            System.out.println("Server ready.........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

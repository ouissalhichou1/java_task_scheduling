package Slave;

import RessourcesForRMI.SlaveDataList;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class Slave {
    static int startingPort = 1099;  // Starting port for the first slave
    static int numSlaves = 4;

    public static void main(String[] args) {
        // Create properties object to store slave information
        Properties prop = new Properties();

        for (int i = 0; i < numSlaves; i++) {
            int currentPort = startingPort + i;

            try {
                // Get the IP address of the local host
                InetAddress localHost = InetAddress.getLocalHost();
                String ipAddress = localHost.getHostAddress();

                // Print the IP address and port of the slave
                System.out.println("Slave " + (i + 1) + " is running at " + ipAddress + ":" + currentPort + "/Slave");

                // Start the RMI registry
                LocateRegistry.createRegistry(currentPort);

                // Store slave information in the properties
                prop.setProperty("Slave" + (i + 1) + ".host", ipAddress);
                prop.setProperty("Slave" + (i + 1) + ".port", String.valueOf(currentPort));

                // Bind the slave objects with unique names based on the filter type
                bindRMIObject("Brightness", ipAddress, currentPort);
                bindRMIObject("Noise", ipAddress, currentPort);
                bindRMIObject("Box", ipAddress, currentPort);
                bindRMIObject("Gaussian", ipAddress, currentPort);
                bindRMIObject("Median", ipAddress, currentPort);

                // Add the slave to the SlaveDataList with availability status set to 1 (available)
                SlaveDataList.addSlave("rmi://" + ipAddress + ":" + currentPort + "/Slave");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Add MainServer information to properties
        prop.setProperty("MainServer.host", "localhost");
        prop.setProperty("MainServer.port", "5200");

        // Store the properties in the cfgMainServer.properties file
        storeProperties(prop);
    }

    // Method to bind RMI objects with unique names based on the filter type
    private static void bindRMIObject(String filterType, String ipAddress, int currentPort) throws Exception {
        String bindingName = "rmi://" + ipAddress + ":" + currentPort + "/Slave" + filterType;

        switch (filterType) {
            case "Brightness":
                ImplBrightnessFilter brightnessFilter = new ImplBrightnessFilter();
                Naming.rebind(bindingName, brightnessFilter);
                break;

            case "Noise":
                ImplNoiseFilter noiseFilter = new ImplNoiseFilter();
                Naming.rebind(bindingName, noiseFilter);
                break;

            case "Box":
                ImplBoxFilter boxFilter = new ImplBoxFilter();
                Naming.rebind(bindingName, boxFilter);
                break;

            case "Gaussian":
                ImplGaussianFilter GaussianFilter = new ImplGaussianFilter();
                Naming.rebind(bindingName, GaussianFilter);
                break;

            case "Median":
                ImplMedianFilter MedianFilter = new ImplMedianFilter();
                Naming.rebind(bindingName, MedianFilter);
                break;

            // Add more cases for other filter types if needed

            default:
                throw new IllegalArgumentException("Unsupported filter type: " + filterType);
        }

        //System.out.println("Filter type '" + filterType + "' bound successfully at " + bindingName);
    }

    // Method to store properties in the cfgMainServer.properties file
    private static void storeProperties(Properties prop) {
        try (OutputStream output = new FileOutputStream("JavaP\\cfgMainServer.properties")) {
            prop.store(output, null);
            System.out.println("Properties stored successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

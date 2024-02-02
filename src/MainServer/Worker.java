package MainServer;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Worker {
    static int Worker_port;
    static String Worker_host;

    public static void main(String[] args) {
        Properties prop = new Properties();
        // Read properties from file
        // ...

        try {
            Worker_port = Integer.parseInt(args[0]);

            InetAddress localHost = InetAddress.getLocalHost();
            // Get the IP address of the local host
            String ipAddress = localHost.getHostAddress();
            // Print the IP address of the local host
            System.out.println("Worker is running at  " + ipAddress + ":" + Worker_port + "/Worker");

            Worker_host = ipAddress;

            LocateRegistry.createRegistry(Worker_port);
            ImplFilters ob = new ImplFilters();

            Naming.rebind("rmi://" + Worker_host + ":" + Worker_port + "/Worker", ob);

            System.out.println("server ready.........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ImplFilters extends java.rmi.server.UnicastRemoteObject implements RessourcesForRMI.Filters {
        public ImplFilters() throws RemoteException {
            super();
        }

        @Override
        public SubMatrix applyFilter(SubMatrix inputSubMatrix, int[][] ker) {
            // Your filter logic here
            return null;
        }
    }

    public static ArrayList<SubMatrix> divideImage(int[][] image, int numWorkers) {
        int rows = image.length;
        int cols = image[0].length;
        int rowsPerWorker = rows / numWorkers;
        int remainingRows = rows % numWorkers;

        ArrayList<SubMatrix> subMatrices = new ArrayList<>();
        int startRow = 0;
        int index = 0;

        for (int i = 0; i < numWorkers; i++) {
            int endRow = startRow + rowsPerWorker;
            if (remainingRows > 0) {
                endRow++;
                remainingRows--;
            }

            subMatrices.add(new SubMatrix(image, startRow, endRow, index));
            startRow = endRow;
            index++;
        }

        return subMatrices;
    }

    public static class SubMatrix implements Serializable {
        public int[][] matrix;
        public int startRow;
        public int endRow;
        public int index;

        public SubMatrix(int[][] matrix, int startRow, int endRow, int index) {
            this.matrix = new int[endRow - startRow + 2][matrix[0].length + 2];
            for (int[] row : this.matrix) {
                Arrays.fill(row, 0);
            }

            this.startRow = startRow;
            this.endRow = endRow;
            this.index = index;

            for (int i = startRow; i < endRow; i++) {
                System.arraycopy(matrix[i], 0, this.matrix[i - startRow], 0, matrix[i].length);
            }
        }
    }
}

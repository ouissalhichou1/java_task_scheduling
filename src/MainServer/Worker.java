package MainServer;

import RessourcesForRMI.Filters;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
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

    public static class ImplFilters extends UnicastRemoteObject implements Filters {

        public ImplFilters() throws RemoteException {
            super();
        }

        @Override
        public SubMatrix applyFilter(SubMatrix inputSubMatrix, int[][] ker) throws RemoteException {
            int[][] inputMatrix = inputSubMatrix.matrix;
            int height = inputMatrix.length;
            int width = inputMatrix[0].length;

            int rows = ker.length;
            int columns = ker[0].length;

            float[] arraykernel = new float[rows * columns];
            int index = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    arraykernel[index++] = ker[i][j];
                }
            }

            Kernel kernel = new Kernel(3, 3, arraykernel);
            ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage inputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    inputImage.setRGB(x, y, inputMatrix[y][x]);
                }
            }

            BufferedImage outputBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            convolveOp.filter(inputImage, outputBufferedImage);
            int[][] outputMatrix = new int[height][width];
            for (int y = 1; y < height; y++) {
                for (int x = 1; x < width; x++) {
                    outputMatrix[y - 1][x - 1] = outputBufferedImage.getRGB(x, y);
                }
            }

            inputSubMatrix.matrix = outputMatrix;
            return inputSubMatrix;
        }
    }
}

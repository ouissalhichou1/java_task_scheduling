package MainServer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import Ressources.*;
import RessourcesForRMI.BrightnessFilter;
import RessourcesForRMI.NoiseFilter;
import RessourcesForRMI.BoxFilter;
import RessourcesForRMI.GaussianFilter;
import RessourcesForRMI.MedianFilter;
import RessourcesForRMI.SlaveData;
import RessourcesForRMI.SlaveDataList;

public class Worker implements Runnable {
    private TaskQueue taskQueue;

    public Worker(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task newTask = taskQueue.take();
                Socket soc = newTask.client;
                System.out.println("+(" + (newTask.TaskId) + ") : Entered");

                ObjectOutputStream d = new ObjectOutputStream(soc.getOutputStream());
                d.writeObject("active");

                ObjectInputStream dis = new ObjectInputStream(soc.getInputStream());
                String taskName = (String) dis.readObject();

                System.out.println("+(" + (newTask.TaskId) + ") : Task is " + taskName);
                if (taskName.compareToIgnoreCase("NOISE") == 0){

                    DataNoise dataNoise = (DataNoise) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    // Check image size difficulty
                    int w = dataNoise.image[0].length;
                    int h = dataNoise.image.length;

                    // Call RMI
                    ArrayList<SlaveData> dispoSlaves;
                    // Try to connect to Slaves, if there is no Slave, do sleep and try again for 5 times
                    int attempts = 0;
                    do {
                        if (attempts != 0)
                            Thread.sleep(2000);
                        dispoSlaves = SlaveDataList.DispoSlaves();
                    } while (dispoSlaves.size() == 0 && attempts++ < 5);

                    int numberOfSlaves =  dispoSlaves.size();

                    // Ensure numberOfSlaves is greater than zero
                    if (numberOfSlaves > 0) {
                        ArrayList<ImageDivider.SubMatrix> allSubMatrix = new ArrayList<>();
                        // Divide image
                        ArrayList<ImageDivider.SubMatrix> sub = ImageDivider.divide(dataNoise.image, numberOfSlaves);

                        for (int i = 0; i < numberOfSlaves; i++) {
                            SlaveData tmpSlave = dispoSlaves.get(i);

                            try {
                                // Use the complete RMI URL when looking up the objects
                                String registryHost = "192.168.56.1"; // Replace with the actual IP address or hostname of your RMI registry
                                int registryPort = 1099; // Replace with the actual port of your RMI registry
                                String objectName = "SlaveNoise";

                                String registryURL = String.format("rmi://%s:%d/%s", registryHost, registryPort, objectName);



                                Object object = Naming.lookup(registryURL);





                                if (object instanceof NoiseFilter) {
                                    NoiseFilter stub = (NoiseFilter) object;
                                    // Apply Noise filter on each submatrix
                                    ImageDivider.SubMatrix result = stub.applyNoise(sub.get(i),
                                            dataNoise.density);
                                    allSubMatrix.add(result);
                                } else {
                                    // handle error or throw an exception
                                    System.out.println("Error: RMI interface not compatible.");
                                }
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            }
                        }

                        // Merge submatrices to get the final image
                        int[][] NoiseImage = ImageDivider.merge(allSubMatrix, dataNoise.image.length, dataNoise.image[0].length);

                        // Send the result back to the client
                        DataResult objectToSend = new DataResult(NoiseImage);
                        ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                        oos.writeObject(objectToSend);
                        System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                    } else {
                        System.out.println("Error: Number of slaves is zero.");
                        // Handle the error appropriately, perhaps by sending an error response to the client.
                    }

                } else if (taskName.compareToIgnoreCase("GRAY FILTER") == 0) {
                    // Processing for gray filter task

                    DataGray dataGray = (DataGray) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    int[][] grayImage = DataGray.convertToGrayscale(dataGray.image);
                    DataResult objectToSend = new DataResult(grayImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                } else if (taskName.compareToIgnoreCase("BRIGHTNESS FILTER") == 0) {
                    // Processing for brightness filter task
                    Databright dataBright = (Databright) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    // Check image size difficulty
                    int w = dataBright.image[0].length;
                    int h = dataBright.image.length;

                    // Call RMI
                    ArrayList<SlaveData> dispoSlaves;
                    // Try to connect to Slaves, if there is no Slave, do sleep and try again for 5 times
                    int attempts = 0;
                    do {
                        if (attempts != 0)
                            Thread.sleep(2000);
                        dispoSlaves = SlaveDataList.DispoSlaves();
                    } while (dispoSlaves.size() == 0 && attempts++ < 5);

                    int numberOfSlaves = dispoSlaves.size();

                    // Ensure numberOfSlaves is greater than zero
                    if (numberOfSlaves > 0) {
                        ArrayList<ImageDivider.SubMatrix> allSubMatrix = new ArrayList<>();
                        // Divide image
                        ArrayList<ImageDivider.SubMatrix> sub = ImageDivider.divide(dataBright.image, numberOfSlaves);

                        for (int i = 0; i < numberOfSlaves; i++) {
                            SlaveData tmpSlave = dispoSlaves.get(i);

                            try {
                                // Use the complete RMI URL when looking up the objects
                                String registryHost = "192.168.56.1"; // Replace with the actual IP address or hostname of your RMI registry
                                int registryPort = 1099; // Replace with the actual port of your RMI registry
                                String objectName = "SlaveBrightness";

                                String registryURL = String.format("rmi://%s:%d/%s", registryHost, registryPort, objectName);



                                Object object = Naming.lookup(registryURL);







                                if (object instanceof BrightnessFilter) {
                                    BrightnessFilter stub = (BrightnessFilter) object;
                                    // Apply brightness filter on each submatrix
                                    ImageDivider.SubMatrix result = stub.applyBrightness(sub.get(i),
                                            dataBright.density);
                                    allSubMatrix.add(result);
                                } else {
                                    // handle error or throw an exception
                                    System.out.println("Error: RMI interface not compatible.");
                                }
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            }
                        }

                        // Merge submatrices to get the final image
                        int[][] brightnessImage = ImageDivider.merge(allSubMatrix, dataBright.image.length,
                                dataBright.image[0].length);

                        // Send the result back to the client
                        DataResult objectToSend = new DataResult(brightnessImage);
                        ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                        oos.writeObject(objectToSend);
                        System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                    } else {
                        System.out.println("Error: Number of slaves is zero.");
                        // Handle the error appropriately, perhaps by sending an error response to the client.
                    }
                }  else if (taskName.compareToIgnoreCase("Box Blur") == 0){

                 DataBox dataBox = (DataBox) dis.readObject();
                 System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                 // Check image size difficulty
                 int w = dataBox.image[0].length;
                 int h = dataBox.image.length;

                 // Call RMI
                 ArrayList<SlaveData> dispoSlaves;
                 // Try to connect to Slaves, if there is no Slave, do sleep and try again for 5 times
                 int attempts = 0;
                 do {
                     if (attempts != 0)
                        Thread.sleep(2000);
                     dispoSlaves = SlaveDataList.DispoSlaves();
                 } while (dispoSlaves.size() == 0 && attempts++ < 5);

                 int numberOfSlaves =  dispoSlaves.size();

                 // Ensure numberOfSlaves is greater than zero
                 if (numberOfSlaves > 0) {
                     ArrayList<ImageDivider.SubMatrix> allSubMatrix = new ArrayList<>();
                     // Divide image
                     ArrayList<ImageDivider.SubMatrix> sub = ImageDivider.divide(dataBox.image, numberOfSlaves);

                     for (int i = 0; i < numberOfSlaves; i++) {
                         SlaveData tmpSlave = dispoSlaves.get(i);

                         try {
                             // Use the complete RMI URL when looking up the objects
                             String registryHost = "192.168.56.1"; // Replace with the actual IP address or hostname of your RMI registry
                             int registryPort = 1099; // Replace with the actual port of your RMI registry
                             String objectName = "SlaveBox";

                             String registryURL = String.format("rmi://%s:%d/%s", registryHost, registryPort, objectName);



                             Object object = Naming.lookup(registryURL);







                             if (object instanceof BoxFilter) {
                                 BoxFilter stub = (BoxFilter) object;
                                 // Apply Box filter on each submatrix
                                 ImageDivider.SubMatrix result = stub.applyBox(sub.get(i),
                                         dataBox.density);
                                 allSubMatrix.add(result);
                             } else {
                                 // handle error or throw an exception
                                 System.out.println("Error: RMI interface not compatible.");
                             }
                         } catch (NotBoundException e) {
                             e.printStackTrace();
                         }
                     }

                      // Merge submatrices to get the final image
                      int[][] BoxImage = ImageDivider.merge(allSubMatrix, dataBox.image.length, dataBox.image[0].length);

                      // Send the result back to the client
                      DataResult objectToSend = new DataResult(BoxImage);
                      ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                      oos.writeObject(objectToSend);
                      System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                 } else {
                      System.out.println("Error: Number of slaves is zero.");
                      // Handle the error appropriately, perhaps by sending an error response to the client.
                 }


                // Processing for box blur task
                } else if (taskName.compareToIgnoreCase("Gaussian Blur") == 0) {
                    // Processing for Gaussian blur task
                    DataGaussian dataGaussian = (DataGaussian) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    // Check image size difficulty
                    int w = dataGaussian.image[0].length;
                    int h = dataGaussian.image.length;

                    // Call RMI
                    ArrayList<SlaveData> dispoSlaves;
                    // Try to connect to Slaves, if there is no Slave, do sleep and try again for 5 times
                    int attempts = 0;
                    do {
                        if (attempts != 0)
                            Thread.sleep(2000);
                        dispoSlaves = SlaveDataList.DispoSlaves();
                    } while (dispoSlaves.size() == 0 && attempts++ < 5);

                    int numberOfSlaves = dispoSlaves.size();

                    // Ensure numberOfSlaves is greater than zero
                    if (numberOfSlaves > 0) {
                        ArrayList<ImageDivider.SubMatrix> allSubMatrix = new ArrayList<>();
                        // Divide image
                        ArrayList<ImageDivider.SubMatrix> sub = ImageDivider.divide(dataGaussian.image, numberOfSlaves);

                        for (int i = 0; i < numberOfSlaves; i++) {
                            SlaveData tmpSlave = dispoSlaves.get(i);

                            try {
                                // Use the complete RMI URL when looking up the objects
                                String registryHost = "192.168.56.1"; // Replace with the actual IP address or hostname of your RMI registry
                                int registryPort = 1099; // Replace with the actual port of your RMI registry
                                String objectName = "SlaveGaussian";

                                String registryURL = String.format("rmi://%s:%d/%s", registryHost, registryPort, objectName);



                                Object object = Naming.lookup(registryURL);







                                if (object instanceof GaussianFilter) {
                                    GaussianFilter stub = (GaussianFilter) object;
                                    // Apply brightness filter on each submatrix
                                    ImageDivider.SubMatrix result = stub.applyGaussian(sub.get(i),
                                            dataGaussian.density);
                                    allSubMatrix.add(result);
                                } else {
                                    // handle error or throw an exception
                                    System.out.println("Error: RMI interface not compatible.");
                                }
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            }
                        }

                        // Merge submatrices to get the final image
                        int[][] GaussianImage = ImageDivider.merge(allSubMatrix, dataGaussian.image.length,
                                dataGaussian.image[0].length);

                        // Send the result back to the client
                        DataResult objectToSend = new DataResult(GaussianImage);
                        ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                        oos.writeObject(objectToSend);
                        System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                    } else {
                        System.out.println("Error: Number of slaves is zero.");
                        // Handle the error appropriately, perhaps by sending an error response to the client.
                    }

                } else if (taskName.compareToIgnoreCase("Median Blur") == 0) {
                    // Processing for median blur task

                    DataMedian dataMedian = (DataMedian) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    // Check image size difficulty
                    int w = dataMedian.image[0].length;
                    int h = dataMedian.image.length;

                    // Call RMI
                    ArrayList<SlaveData> dispoSlaves;
                    // Try to connect to Slaves, if there is no Slave, do sleep and try again for 5 times
                    int attempts = 0;
                    do {
                        if (attempts != 0)
                            Thread.sleep(2000);
                        dispoSlaves = SlaveDataList.DispoSlaves();
                    } while (dispoSlaves.size() == 0 && attempts++ < 5);

                    int numberOfSlaves = dispoSlaves.size();

                    // Ensure numberOfSlaves is greater than zero
                    if (numberOfSlaves > 0) {
                        ArrayList<ImageDivider.SubMatrix> allSubMatrix = new ArrayList<>();
                        // Divide image
                        ArrayList<ImageDivider.SubMatrix> sub = ImageDivider.divide(dataMedian.image, numberOfSlaves);

                        for (int i = 0; i < numberOfSlaves; i++) {
                            SlaveData tmpSlave = dispoSlaves.get(i);

                            try {
                                // Use the complete RMI URL when looking up the objects
                                String registryHost = "192.168.56.1"; // Replace with the actual IP address or hostname of your RMI registry
                                int registryPort = 1099; // Replace with the actual port of your RMI registry
                                String objectName = "SlaveMedian";

                                String registryURL = String.format("rmi://%s:%d/%s", registryHost, registryPort, objectName);



                                Object object = Naming.lookup(registryURL);







                                if (object instanceof MedianFilter) {
                                    MedianFilter stub = (MedianFilter) object;
                                    // Apply Median filter on each submatrix
                                    ImageDivider.SubMatrix result = stub.applyMedian(sub.get(i),
                                            dataMedian.density);
                                    allSubMatrix.add(result);
                                } else {
                                    // handle error or throw an exception
                                    System.out.println("Error: RMI interface not compatible.");
                                }
                            } catch (NotBoundException e) {
                                e.printStackTrace();
                            }
                        }

                        // Merge submatrices to get the final image
                        int[][] MedianImage = ImageDivider.merge(allSubMatrix, dataMedian.image.length,
                                dataMedian.image[0].length);

                        // Send the result back to the client
                        DataResult objectToSend = new DataResult(MedianImage);
                        ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                        oos.writeObject(objectToSend);
                        System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                    } else {
                        System.out.println("Error: Number of slaves is zero.");
                        // Handle the error appropriately, perhaps by sending an error response to the client.
                    }
                } else if (taskName.compareToIgnoreCase("Conte Crayon") == 0) {
                    // Processing for Conte Crayon task

                    DataConte dataconte = (DataConte) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    int[][] conteImage = DataConte.applyConteFilter(dataconte.image);
                    DataResult objectToSend = new DataResult(conteImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                } else if (taskName.compareToIgnoreCase("Chrome") == 0) {
                    // Processing for Chrome task

                    DataChrome datachrome = (DataChrome) dis.readObject();
                    System.out.println("+(" + (newTask.TaskId) + ") : All data received");

                    int[][] chromeImage = DataChrome.applyChromeFilter(datachrome.image);
                    DataResult objectToSend = new DataResult(chromeImage);

                    ObjectOutputStream oos = new ObjectOutputStream(soc.getOutputStream());
                    oos.writeObject(objectToSend);

                    System.out.println("+(" + (newTask.TaskId) + ") : Result sent");
                }
            } // Close the while loop here
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

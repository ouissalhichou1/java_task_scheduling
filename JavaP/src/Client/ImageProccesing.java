package Client;

import Ressources.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ImageProccesing extends JFrame {
    private static JLabel originalImageLabel;
    private static JLabel filteredImageLabel;
    private static JComboBox<String> tasksComboBox1;
    private static JComboBox<String> tasksComboBox2;
    private static JComboBox<String> tasksComboBox3;
    private static JFrame F;
    static BufferedImage originalImage;
    static BufferedImage filteredImage;
    static String value;
    static String task;
    static int[][] kernel = new int[3][3];

    static int MainServer_port;
    static String MainServer_host;
    public ImageProccesing(String[] args){
        Properties prop = new Properties();
        FileInputStream ip;
        String FileConfiguration = "JavaP\\cfgClient.properties";
        if (args.length > 0)
            FileConfiguration = args[0];
            try {
                ip = new FileInputStream(FileConfiguration);
                prop.load(ip);
            } catch (Exception e2) {
                System.exit(0);
            }

        MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
        MainServer_host = prop.getProperty("MainServer.host");

        F = new JFrame();

        F.setTitle("image proccesing");
        F.setSize(600, 600);
        F.setDefaultCloseOperation(EXIT_ON_CLOSE);
        F.setExtendedState(JFrame.MAXIMIZED_BOTH);
        F.setLayout(new BorderLayout(20, 20));
        F.getContentPane().setBackground(new Color(0xCDC9EA)); // Set background color

        originalImageLabel = new JLabel();
        filteredImageLabel = new JLabel();

        JPanel imagesPanel = new JPanel(new GridLayout(1, 2));
        imagesPanel.add(originalImageLabel);
        imagesPanel.add(filteredImageLabel);
        F.add(imagesPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(new Color(0xCDC9EA)); // Set background color
        F.add(controlsPanel, BorderLayout.NORTH);

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setBackground(Color.decode("#B3B0CD")); // Set button color
        chooseImageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(F);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        originalImage = ImageIO.read(fileChooser.getSelectedFile());
                        int newWidth = 640;
                        int newHeight = 640;
                        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        originalImageLabel.setIcon(new ImageIcon(resizedImage));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        });
        controlsPanel.add(chooseImageButton);

        tasksComboBox1 = new JComboBox<>(
                new String[] {  "NOISE", "GRAY FILTER","BRIGHTNESS FILTER" });
        tasksComboBox1.setBackground(Color.decode("#B3B0CD")); // Set background color
        controlsPanel.add(tasksComboBox1);

        tasksComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(F, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                task = (String) tasksComboBox1.getSelectedItem();

                if (task.equals("NOISE")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of noise ex : {0.02}:");
                }
                if (task.equals("BRIGHTNESS FILTER")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of BRIGHTNESS ex :{100} :");
                }

            }
        });
        tasksComboBox2 = new JComboBox<>(
                new String[] {"Gaussian Blur","Median Blur","Box Blur" });
        tasksComboBox2.setBackground(Color.decode("#B3B0CD")); // Set background color
        controlsPanel.add(tasksComboBox2);
        tasksComboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(F, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                task = (String) tasksComboBox2.getSelectedItem();
                if (task.equals("Median Blur")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of blur ex : {50}:");
                }

                if (task.equals("Gaussian Blur")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of blur ex : {40}:");
                }
                if (task.equals("Box Blur")) {
                    value = JOptionPane.showInputDialog(F, "Enter level of blur ex : {10}:");
                }

            }
        });


        tasksComboBox3 = new JComboBox<>(
                new String[] {"Conte Crayon","Chrome" });
        tasksComboBox3.setBackground(Color.decode("#B3B0CD")); // Set background color
        controlsPanel.add(tasksComboBox3);
        tasksComboBox3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(F, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                task = (String) tasksComboBox3.getSelectedItem();



                if (task.equals("Conte Crayon")) {
                }
                if (task.equals("Chrome")) {
                }
            }
        });
        JButton processImageButton = new JButton("Process Image");
        processImageButton.setBackground(Color.decode("#B3B0CD")); // Set button color
        controlsPanel.add(processImageButton);
        F.setVisible(true);
        processImageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(F, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    System.out.println("Adresse de MainServer");
                    System.out.println("=> " + MainServer_host + ":" + MainServer_port);
                    Socket socket = new Socket(MainServer_host, MainServer_port);
                    System.out.println("connected");
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    String state = (String) input.readObject();
                    int[][] img = bufferedImageToIntArray(originalImage);
                    if (state.compareToIgnoreCase("active") == 0) {
                        output.writeObject(task);

                        if (task.compareToIgnoreCase("NOISE") == 0) {
                            DataNoise dataNoise = new DataNoise(img, Double.parseDouble(value));
                            output.writeObject(dataNoise);
                        }

                        if (task.compareToIgnoreCase("GRAY FILTER") == 0) {
                            DataGray dataGray = new DataGray(img);
                            output.writeObject(dataGray);
                        }

                        if (task.compareToIgnoreCase("BRIGHTNESS FILTER") == 0) {
                            Databright dataNoise = new Databright(img, Double.parseDouble(value));
                            output.writeObject(dataNoise);
                        }
                        if (task.compareToIgnoreCase("Box Blur") == 0) {
                            DataBox dataBox = new DataBox(img, Double.parseDouble(value));
                            output.writeObject(dataBox);
                        }
                        if (task.compareToIgnoreCase("Chrome") == 0) {
                            DataChrome dataChrome = new DataChrome(img);
                            output.writeObject(dataChrome);
                        }

                        if (task.compareToIgnoreCase("Conte Crayon") == 0) {
                            DataConte dataConte = new DataConte(img);
                            output.writeObject(dataConte);
                        }
                        if (task.compareToIgnoreCase("Gaussian Blur") == 0) {
                            DataGaussian dataGaussian = new DataGaussian(img, Double.parseDouble(value));
                            output.writeObject(dataGaussian);
                        }
                        if (task.compareToIgnoreCase("Median Blur") == 0) {
                            DataMedian dataMedian = new DataMedian(img, Double.parseDouble(value));
                            output.writeObject(dataMedian);
                        }

                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        DataResult pixels = (DataResult) in.readObject();
                        filteredImage = intArrayToBufferedImage(pixels.image);
                        int newWidth = 640;
                        int newHeight = 640;
                        Image resizedImage = filteredImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        filteredImageLabel.setIcon(new ImageIcon(resizedImage));

                    }
                    socket.close();
                } catch (Exception r) {
                    System.out.println(r.getMessage());
                }

            }
        });
    }

    public int[][] bufferedImageToIntArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = image.getRGB(x, y);
            }
        }
        return result;
    }

    public static BufferedImage intArrayToBufferedImage(int[][] pixels) {
        int height = pixels.length;
        int width = pixels[0].length;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result.setRGB(x, y, pixels[y][x]);
            }
        }
        return result;
    }
}

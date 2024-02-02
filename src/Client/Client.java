package Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import javax.imageio.ImageIO;

import Ressources.Databright;
import Ressources.DataConvolution;
import Ressources.DataGray;
import Ressources.DataNoise;
import Ressources.DataResult;
import Ressources.Datainvert;
import Ressources.DataSapia;

public class Client extends JFrame {

    static int MainServer_port;
    static String MainServer_host;

    public Client() {
        setTitle("Choose Operation");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton matrixButton = new JButton("Matrix Calculations");
        JButton imageButton = new JButton("Image Filtering");

        matrixButton.addActionListener(e -> {
            // Open JFrame for matrix calculations
            new MatrixCalculationsFrame();
            setVisible(false);
        });

        imageButton.addActionListener(e -> {
            // Open existing image filtering JFrame
            new ImageFilteringFrame();
            setVisible(false);
        });

        panel.add(matrixButton);
        panel.add(imageButton);
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        FileInputStream ip;
        String FileConfiguration = "cfgClient.properties";
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

        // Establish socket connection to the server
        try (Socket socket = new Socket(MainServer_host, MainServer_port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            // Communication with the server can be performed here

            // Open the client GUI
            SwingUtilities.invokeLater(() -> new Client());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MatrixCalculationsFrame extends JFrame {

    private JComboBox<String> operationComboBox;
    private JComboBox<Integer> dimensionComboBox;
    private JTextField[][] matrixFields;
    private JButton calculateButton;
    private JLabel resultLabel;

    public MatrixCalculationsFrame() {
        setTitle("Matrix Calculations");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        // Operation ComboBox
        operationComboBox = new JComboBox<>(new String[]{"Addition", "Multiplication", "Subtraction"});
        panel.add(new JLabel("Operation:"));
        panel.add(operationComboBox);

        // Dimension ComboBox
        dimensionComboBox = new JComboBox<>(new Integer[]{2, 3, 4});
        panel.add(new JLabel("Dimension:"));
        panel.add(dimensionComboBox);

        // Matrix Fields
        int maxDimension = 4; // Maximum dimension supported
        matrixFields = new JTextField[maxDimension][maxDimension];
        for (int i = 0; i < maxDimension; i++) {
            for (int j = 0; j < maxDimension; j++) {
                matrixFields[i][j] = new JTextField(5);
                matrixFields[i][j].setEnabled(false); // Initially disabled
            }
        }

        // Button to populate matrix fields based on selected dimension
        JButton populateMatrixButton = new JButton("Populate Matrix");
        populateMatrixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dimension = (int) dimensionComboBox.getSelectedItem();
                enableMatrixFields(dimension);
            }
        });
        panel.add(populateMatrixButton);

        // Button to perform calculation
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform calculation and update resultLabel
                String operation = (String) operationComboBox.getSelectedItem();
                int dimension = (int) dimensionComboBox.getSelectedItem();
                int[][] matrixA = getMatrixFromFields(dimension, 0);
                int[][] matrixB = getMatrixFromFields(dimension, dimension * dimension);
                int[][] result = calculateMatrix(matrixA, matrixB, operation);
                updateResultLabel(result);
            }
        });
        panel.add(calculateButton);

        // Result Label
        resultLabel = new JLabel("Result:");
        panel.add(resultLabel);

        add(panel);

        setVisible(true);
    }

    // Enable/disable matrix fields based on the selected dimension
    private void enableMatrixFields(int dimension) {
        for (int i = 0; i < matrixFields.length; i++) {
            for (int j = 0; j < matrixFields[i].length; j++) {
                matrixFields[i][j].setEnabled(i < dimension && j < dimension);
            }
        }
    }

    // Get matrix data from the text fields
    private int[][] getMatrixFromFields(int dimension, int startIndex) {
        int[][] matrix = new int[dimension][dimension];
        int index = startIndex;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                String text = matrixFields[i][j].getText();
                if (!text.isEmpty()) {
                    matrix[i][j] = Integer.parseInt(text);
                }
                index++;
            }
        }
        return matrix;
    }

    // Perform matrix calculation based on the selected operation
    private int[][] calculateMatrix(int[][] matrixA, int[][] matrixB, String operation) {
        int dimension = matrixA.length;
        int[][] result = new int[dimension][dimension];
        if (operation.equals("Addition")) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    result[i][j] = matrixA[i][j] + matrixB[i][j];
                }
            }
        } else if (operation.equals("Multiplication")) {
            // Perform multiplication
        } else if (operation.equals("Subtraction")) {
            // Perform subtraction
        }
        return result;
    }

    // Update the result label with the calculated matrix
    private void updateResultLabel(int[][] result) {
        StringBuilder sb = new StringBuilder("<html>");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                sb.append(result[i][j]).append(" ");
            }
            sb.append("<br>");
        }
        sb.append("</html>");
        resultLabel.setText(sb.toString());
    }
}

class ImageFilteringFrame extends JFrame {
    private JLabel originalImageLabel;
    private JLabel filteredImageLabel;
    private JComboBox<String> tasksComboBox;
    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    private String value;
    private String task;
    private int[][] kernel = new int[3][3];

    public ImageFilteringFrame() {
        setTitle("Image Filtering");
        setSize(1370, 703);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(20, 20));

        originalImageLabel = new JLabel();
        filteredImageLabel = new JLabel();

        JPanel imagesPanel = new JPanel(new GridLayout(1, 2));
        imagesPanel.add(originalImageLabel);
        imagesPanel.add(filteredImageLabel);
        add(imagesPanel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        add(controlsPanel, BorderLayout.NORTH);

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ImageFilteringFrame.this);
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

        tasksComboBox = new JComboBox<>(
                new String[]{"CONVOLUTION FILTER", "NOISE SALT AND PEPPER", "GRAY FILTER", "SEPIA FILTER",
                        "INVERT FILTER", "BRIGHTNESS FILTER"});
        controlsPanel.add(tasksComboBox);

        tasksComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(ImageFilteringFrame.this, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                task = (String) tasksComboBox.getSelectedItem();
                if (task.equals("CONVOLUTION FILTER")) {
                    JTextField[][] textFields = new JTextField[3][3];
                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    for (int i = 0; i < 3; i++) {
                        JPanel rowPanel = new JPanel();
                        for (int j = 0; j < 3; j++) {
                            textFields[i][j] = new JTextField(5);
                            rowPanel.add(textFields[i][j]);
                        }
                        panel.add(rowPanel);
                    }
                    int result = JOptionPane.showConfirmDialog(null, panel, "Enter Matrix",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                kernel[i][j] = Integer.parseInt(textFields[i][j].getText());
                            }
                        }
                    }
                }
                if (task.equals("NOISE SALT AND PEPPER")) {
                    value = JOptionPane.showInputDialog(ImageFilteringFrame.this, "Enter level of noise ex : {0.02}:");
                    System.out.println(value);
                }
                if (task.equals("BRIGHTNESS FILTER")) {
                    value = JOptionPane.showInputDialog(ImageFilteringFrame.this, "Enter level of BRIGHTNESS ex :{100} :");
                    System.out.println(value);
                }
            }
        });

        JButton processImageButton = new JButton("Process Image");
        controlsPanel.add(processImageButton);
        setVisible(true);
        processImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(ImageFilteringFrame.this, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Socket socket = null;
                try {
                    System.out.println("Adresse de MainServer");
                    System.out.println("=> " + Client.MainServer_host + ":" + Client.MainServer_port);
                    socket = new Socket(Client.MainServer_host, Client.MainServer_port);
                    System.out.println("connected");
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    String state = (String) input.readObject();
                    int[][] img = bufferedImageToIntArray(originalImage); // Use the local method here
                    if (state.compareToIgnoreCase("active") == 0) {
                        output.writeObject(task);
                        if (task.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                            DataConvolution dataConvolution = new DataConvolution(img, kernel);
                            output.writeObject(dataConvolution);
                        }
                        if (task.compareToIgnoreCase("NOISE SALT AND PEPPER") == 0) {
                            DataNoise dataNoise = new DataNoise(img, Double.parseDouble(value));
                            output.writeObject(dataNoise);
                        }
                        if (task.compareToIgnoreCase("GRAY FILTER") == 0) {
                            DataGray dataGray = new DataGray(img);
                            output.writeObject(dataGray);
                        }
                        if (task.compareToIgnoreCase("SEPIA FILTER") == 0) {
                            DataSapia sapil = new DataSapia(img);
                            output.writeObject(sapil);
                        }
                        if (task.compareToIgnoreCase("INVERT FILTER") == 0) {
                            Datainvert inverse = new Datainvert(img);
                            output.writeObject(inverse);
                        }
                        if (task.compareToIgnoreCase("BRIGHTNESS FILTER") == 0) {
                            Databright dataNoise = new Databright(img, Double.parseDouble(value));
                            output.writeObject(dataNoise);
                        }
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        DataResult pixels = (DataResult) in.readObject();
                        filteredImage = intArrayToBufferedImage(pixels.image); // Use the local method here
                        int newWidth = 640;
                        int newHeight = 640;
                        Image resizedImage = filteredImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                        filteredImageLabel.setIcon(new ImageIcon(resizedImage));
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // Method to convert BufferedImage to int array
    public static int[][] bufferedImageToIntArray(BufferedImage image) {
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

    // Method to convert int array to BufferedImage
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

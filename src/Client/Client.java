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

public class Client extends JFrame {
    static int MainServer_port;
    static String MainServer_host;
    public Client() {
        setTitle("Choose Operation");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        String FileConfiguration = "cfgClient.properties";
        if (args.length > 0)
            FileConfiguration = args[0];
        try (FileInputStream ip = new FileInputStream(FileConfiguration)) {
            prop.load(ip);
        } catch (IOException e2) {
            e2.printStackTrace();
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
            SwingUtilities.invokeLater(Client::new);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class MatrixCalculationsFrame extends JFrame {

        private JComboBox<String> operationComboBox;
        private JComboBox<Integer> rowComboBoxA;
        private JComboBox<Integer> colComboBoxA;
        private JComboBox<Integer> rowComboBoxB;
        private JComboBox<Integer> colComboBoxB;

        public MatrixCalculationsFrame() {
            setTitle("Matrix Calculations");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 2, 10, 10)); // Adjust the horizontal and vertical gaps

            // Initialize components
            operationComboBox = new JComboBox<>(new String[]{"Addition", "Subtraction", "Multiplication"});
            rowComboBoxA = new JComboBox<>();
            colComboBoxA = new JComboBox<>();
            rowComboBoxB = new JComboBox<>();
            colComboBoxB = new JComboBox<>();

            // Add components to the panel
            panel.add(new JLabel("Operation:"));
            panel.add(operationComboBox);
            panel.add(new JLabel("Rows (Matrix A):"));
            panel.add(rowComboBoxA);
            panel.add(new JLabel("Columns (Matrix A):"));
            panel.add(colComboBoxA);
            panel.add(new JLabel("Rows (Matrix B):"));
            panel.add(rowComboBoxB);
            panel.add(new JLabel("Columns (Matrix B):"));
            panel.add(colComboBoxB);

            // Button to confirm dimensions and proceed
            JButton confirmButton = new JButton("Confirm");
            confirmButton.addActionListener(e -> {
                // Get selected dimensions and operation
                String operation = (String) operationComboBox.getSelectedItem();
                int rowsA = (int) rowComboBoxA.getSelectedItem();
                int colsA = (int) colComboBoxA.getSelectedItem();
                int rowsB = (int) rowComboBoxB.getSelectedItem();
                int colsB = (int) colComboBoxB.getSelectedItem();

                // Check if dimensions are compatible with the operation
                boolean dimensionsValid = checkDimensions(operation, rowsA, colsA, rowsB, colsB);
                if (dimensionsValid) {
                    // Proceed to the MatrixInputFrame
                    new MatrixInputFrame(operation, rowsA, colsA, rowsB, colsB);
                    setVisible(false); // Hide the current frame
                } else {
                    // Display error message if dimensions are not compatible
                    JOptionPane.showMessageDialog(MatrixCalculationsFrame.this,
                            "Invalid dimensions for the selected operation.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(new JLabel()); // Empty label for alignment
            panel.add(confirmButton);

            // Populate row and column combo boxes with values
            for (int i = 1; i <= 10; i++) {
                rowComboBoxA.addItem(i);
                colComboBoxA.addItem(i);
                rowComboBoxB.addItem(i);
                colComboBoxB.addItem(i);
            }

            add(panel);

            setVisible(true); // Make the frame visible
        }

        // Check if the selected dimensions are compatible with the operation
        private boolean checkDimensions(String operation, int rowsA, int colsA, int rowsB, int colsB) {
            // Implement dimension checking logic based on the chosen operation
            // For example, for multiplication, the number of columns of matrix A should match the number of rows of matrix B
            if (operation.equals("Addition") || operation.equals("Subtraction")) {
                return rowsA == rowsB && colsA == colsB;
            } else if (operation.equals("Multiplication")) {
                return colsA == rowsB;
            }
            return false;
        }
    }
    class MatrixInputFrame extends JFrame {
        private String operation;
        private int rowsA, colsA, rowsB, colsB;
        public MatrixInputFrame(String operation, int rowsA, int colsA, int rowsB, int colsB) {
            this.operation = operation;
            this.rowsA = rowsA;
            this.colsA = colsA;
            this.rowsB = rowsB;
            this.colsB = colsB;

            setTitle("Matrix Input");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 2, 10, 10)); // Adjust the horizontal and vertical gaps

            // Create text fields for matrix A
            JLabel labelA = new JLabel("Enter values for Matrix A:");
            panel.add(labelA);

            // Create text fields for matrix A with consistent column width
            JPanel matrixAPanel = new JPanel(new GridLayout(rowsA, colsA, 5, 5)); // Adjust the horizontal and vertical gaps
            JTextField[][] matrixAFields = new JTextField[rowsA][colsA];
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsA; j++) {
                    matrixAFields[i][j] = new JTextField(5);
                    matrixAPanel.add(matrixAFields[i][j]);
                }
            }
            panel.add(matrixAPanel);

            // Create text fields for matrix B
            JLabel labelB = new JLabel("Enter values for Matrix B:");
            panel.add(labelB);

            // Create text fields for matrix B with consistent column width
            JPanel matrixBPanel = new JPanel(new GridLayout(rowsB, colsB, 5, 5)); // Adjust the horizontal and vertical gaps
            JTextField[][] matrixBFields = new JTextField[rowsB][colsB];
            for (int i = 0; i < rowsB; i++) {
                for (int j = 0; j < colsB; j++) {
                    matrixBFields[i][j] = new JTextField(5);
                    matrixBPanel.add(matrixBFields[i][j]);
                }
            }
            panel.add(matrixBPanel);

            // Button to calculate matrices
            JButton calculateButton = new JButton("Calculate");
            calculateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get matrix data from text fields and perform calculation
                    int[][] matrixA = getMatrixFromFields(matrixAFields, rowsA, colsA);
                    int[][] matrixB = getMatrixFromFields(matrixBFields, rowsB, colsB);
                    // Perform calculation based on the operation
                    // You can implement the calculation logic here
                    // For now, we'll just display a message
                    JOptionPane.showMessageDialog(MatrixInputFrame.this,
                            "Calculation performed. Replace this with actual logic.",
                            "Calculation Result", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            panel.add(new JLabel()); // Empty label for alignment
            panel.add(calculateButton);

            add(panel);

            setVisible(true);
        }

        // Get matrix data from text fields
        private int[][] getMatrixFromFields(JTextField[][] matrixFields, int rows, int cols) {
            int[][] matrix = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String text = matrixFields[i][j].getText();
                    if (!text.isEmpty() && text.matches("\\d+")) { // Check if text is a number
                        matrix[i][j] = Integer.parseInt(text);
                    } else {
                        // Handle invalid input
                        JOptionPane.showMessageDialog(this,
                                "Invalid input. Please enter numbers only.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return null; // Return null if input is invalid
                    }
                }
            }
            return matrix;
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
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

}
package Client;

import Ressources.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageFilteringFrame extends JFrame {

    static int MainServer_port;
    static String MainServer_host;
    private JLabel originalImageLabel;
    private JLabel filteredImageLabel;
    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    private double zoomLevel = 0.5;
    static String value;
    static String task;
    static int[][] kernel = new int[3][3];

    public ImageFilteringFrame() {
        setTitle("Image Filtering");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#C0BCB5")); // Set background color of the frame

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ImageFilteringFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        originalImage = ImageIO.read(fileChooser.getSelectedFile());
                        updateOriginalImage();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Create a panel for process button
        JPanel processButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton processImageButton = new JButton("Process Image");
        processImageButton.setBackground(Color.decode("#4B4B4B")); // Set background color of the button
        processImageButton.setForeground(Color.WHITE); // Set text color of the button
        processImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(ImageFilteringFrame.this, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                sendTaskToServer();
            }
        });
        processButtonPanel.add(processImageButton);
        processButtonPanel.setBackground(Color.decode("#C0BCB5")); // Set background color of the panel

        // Create controls panel for process button
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(processButtonPanel, BorderLayout.NORTH);
        controlsPanel.setBackground(Color.decode("#C0BCB5")); // Set background color of the controls panel
        add(controlsPanel, BorderLayout.NORTH);

        // Create a side panel for filters
        JPanel sidePanel = new JPanel(new GridLayout(0, 1));
        sidePanel.setPreferredSize(new Dimension(200, 0)); // Set preferred width for the side panel
        sidePanel.setBackground(Color.decode("#C0BCB5")); // Set background color of the side panel

        // Create buttons for filters
        JButton convolutionButton = new JButton("Convolution Filter");
        JButton noiseButton = new JButton("Noise Salt and Pepper");
        JButton grayButton = new JButton("Gray Filter");
        JButton sepiaButton = new JButton("Sepia Filter");
        JButton invertButton = new JButton("Invert Filter");
        JButton brightnessButton = new JButton("Brightness Filter");

        // Set colors for filter buttons
        convolutionButton.setBackground(Color.decode("#4B4B4B"));
        convolutionButton.setForeground(Color.WHITE);
        noiseButton.setBackground(Color.decode("#4B4B4B"));
        noiseButton.setForeground(Color.WHITE);
        grayButton.setBackground(Color.decode("#4B4B4B"));
        grayButton.setForeground(Color.WHITE);
        sepiaButton.setBackground(Color.decode("#4B4B4B"));
        sepiaButton.setForeground(Color.WHITE);
        invertButton.setBackground(Color.decode("#4B4B4B"));
        invertButton.setForeground(Color.WHITE);
        brightnessButton.setBackground(Color.decode("#4B4B4B"));
        brightnessButton.setForeground(Color.WHITE);

        // Add action listeners to filter buttons
        convolutionButton.addActionListener(e -> {
            task = "CONVOLUTION FILTER";
            sendTaskToServer();
        });
        noiseButton.addActionListener(e -> {
            task = "NOISE SALT AND PEPPER";
            value = JOptionPane.showInputDialog(ImageFilteringFrame.this, "Enter level of noise ex : {0.02}:");
            sendTaskToServer();
        });
        grayButton.addActionListener(e -> {
            task = "GRAY FILTER";
            sendTaskToServer();
        });
        sepiaButton.addActionListener(e -> {
            task = "SEPIA FILTER";
            sendTaskToServer();
        });
        invertButton.addActionListener(e -> {
            task = "INVERT FILTER";
            sendTaskToServer();
        });
        brightnessButton.addActionListener(e -> {
            task = "BRIGHTNESS FILTER";
            value = JOptionPane.showInputDialog(ImageFilteringFrame.this, "Enter level of BRIGHTNESS ex :{100} :");
            sendTaskToServer();
        });

        // Add buttons to the side panel
        sidePanel.add(convolutionButton);
        sidePanel.add(noiseButton);
        sidePanel.add(grayButton);
        sidePanel.add(sepiaButton);
        sidePanel.add(invertButton);
        sidePanel.add(brightnessButton);

        // Add the side panel to the frame
        add(sidePanel, BorderLayout.WEST);

        // Create a panel for the image display and controls
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 20)); // Add padding of 20 pixels
        imagePanel.setBackground(Color.decode("#C0BCB5")); // Set background color of the image panel

        originalImageLabel = new JLabel();
        filteredImageLabel = new JLabel();

        imagePanel.add(originalImageLabel, BorderLayout.CENTER);
        imagePanel.add(filteredImageLabel, BorderLayout.EAST);

        add(imagePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateOriginalImage() {
        if (originalImage != null) {
            int newWidth = (int) (originalImage.getWidth() * zoomLevel);
            int newHeight = (int) (originalImage.getHeight() * zoomLevel);
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            originalImageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }

    private void sendTaskToServer() {
        try {
            Socket socket = new Socket(MainServer_host, MainServer_port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(task);
            output.writeObject(value);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

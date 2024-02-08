package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageFilteringFrame extends JFrame {
    private JLabel originalImageLabel;
    private JLabel filteredImageLabel;
    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    private double zoomLevel = 1.0;

    public ImageFilteringFrame() {
        setTitle("Image Filtering");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());

        originalImageLabel = new JLabel();
        filteredImageLabel = new JLabel();

        // Add padding to the left panel to move the image to the right
        leftPanel.setBorder(new EmptyBorder(0, 50, 0, 0));
        leftPanel.add(originalImageLabel, BorderLayout.CENTER);
        rightPanel.add(filteredImageLabel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        JPanel controlsPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 50); // Add some padding

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(new ActionListener() {
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
        // Set margin for the button
        chooseImageButton.setMargin(new Insets(10, 20, 10, 20)); // Top, Left, Bottom, Right
        controlsPanel.add(chooseImageButton, gbc);

        gbc.gridy++;
        JButton processImageButton = new JButton("Process Image");
        // Set margin for the button
        processImageButton.setMargin(new Insets(10, 20, 10, 20)); // Top, Left, Bottom, Right
        controlsPanel.add(processImageButton, gbc);

        processImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (originalImage == null) {
                    JOptionPane.showMessageDialog(ImageFilteringFrame.this, "Please select an image.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Send the original image to the server for filtering
                // Implement the logic to send the image to the server and receive the filtered image
                // For now, let's assume we have a method called sendImageToServer(BufferedImage image)
                // and a method called receiveFilteredImag() that returns the filtered image
                filteredImage = sendImageToServer(originalImage);
                updateFilteredImage();
            }
        });

        gbc.gridy++;
        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 50, 200, 100);
        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setMinorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(new ChangeListener() { // Implement ChangeListener interface here
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource(); // Casting the event source to JSlider
                if (!source.getValueIsAdjusting()) {
                    int zoomValue = source.getValue();
                    zoomLevel = zoomValue / 100.0;
                    updateOriginalImage();
                    updateFilteredImage(); // Update filtered image too
                }
            }
        });
        controlsPanel.add(zoomSlider, gbc);

        rightPanel.add(controlsPanel, BorderLayout.CENTER);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu convolutionFilterItem = new JMenu("Convolution Filter");
        JMenu noiseFilterItem = new JMenu("Noise Salt and Pepper");
        JMenu grayFilterItem = new JMenu("Gray Filter");
        JMenu sepiaFilterItem = new JMenu("Sepia Filter");
        JMenu invertFilterItem = new JMenu("Invert Filter");
        JMenu brightnessFilterItem = new JMenu("Brightness Filter");

        menuBar.add(convolutionFilterItem);
        menuBar.add(noiseFilterItem);
        menuBar.add(grayFilterItem);
        menuBar.add(sepiaFilterItem);
        menuBar.add(invertFilterItem);
        menuBar.add(brightnessFilterItem);
        setJMenuBar(menuBar);

        convolutionFilterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Convolution Filter action
            }
        });

        noiseFilterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Noise Filter action
            }
        });

        grayFilterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Gray Filter action
            }
        });

        sepiaFilterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Sepia Filter action
            }
        });

        invertFilterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Invert Filter action
            }
        });

        brightnessFilterItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Brightness Filter action
            }
        });

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

    private void updateFilteredImage() {
        if (filteredImage != null) {
            int newWidth = (int) (filteredImage.getWidth() * zoomLevel);
            int newHeight = (int) (filteredImage.getHeight() * zoomLevel);
            Image scaledImage = filteredImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            filteredImageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }

    // Method to send the original image to the server and receive the filtered image
    private BufferedImage sendImageToServer(BufferedImage originalImage) {
        // Implement the logic to send the image to the server and receive the filtered image
        // For now, let's return the original image itself
        return originalImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ImageFilteringFrame();
        });
    }
}

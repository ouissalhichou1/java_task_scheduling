package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.net.Socket;

public class MatriceInfo implements Serializable {
    private JFrame jf;
    private JPanel allInfo, infoMatrice1, infoMatrice2, btnPanel;
    private JLabel title ;
    private JTextField rowsM1, colsM1, rowsM2, colsM2;
    private JComboBox<String> operationComboBox;
    private int r1,c1,r2,c2;

    private Socket socket;

    public MatriceInfo(Socket s){
        socket = s;
        jf = new JFrame();
        allInfo = new JPanel(new GridLayout( 7, 1));
        // 1
        title = new JLabel("Information des matrices : ");

        // 2
        operationComboBox = new JComboBox<>(new String[]{"Addition", "Multiplication"});
        operationComboBox.setBackground(Color.decode("#B3B0CD"));

        // 3 - matrice 1:
        infoMatrice1 = new JPanel(new GridLayout( 2, 2));
        infoMatrice1.add(new JLabel("Lines of A"));
        infoMatrice1.add(new JLabel("Columns of A"));
        rowsM1 = new JTextField();
        acceptJustNumber(rowsM1);


        colsM1 = new JTextField();
        acceptJustNumber(colsM1);
        infoMatrice1.add(rowsM1);
        infoMatrice1.add(colsM1);


        // 4 - matrice 2:
        infoMatrice2 = new JPanel(new GridLayout( 2, 2));
        infoMatrice2.add(new JLabel("Lines of B"));
        infoMatrice2.add(new JLabel("Columns of B"));
        rowsM2 = new JTextField();
        acceptJustNumber(rowsM2);
        colsM2 = new JTextField();
        acceptJustNumber(colsM2);
        infoMatrice2.add(rowsM2);
        infoMatrice2.add(colsM2);
        colsM2.disable();
        rowsM2.disable();

        JButton submitButton = new JButton("Next");
        submitButton.setBackground(Color.decode("#B3B0CD"));
        btnPanel = new JPanel(new GridLayout( 2, 4));



        btnPanel.setBorder(new EmptyBorder(20, 270, 0, 0));
        btnPanel.add(submitButton);

        // Actions
        operationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) operationComboBox.getSelectedItem();
                System.out.println("Option sélectionnée : " + selectedOption);
                clearData();
                if(selectedOption == "Addition"){
                    colsM2.disable();
                    rowsM2.disable();
                }
                else{
                    colsM2.setEnabled(true);
                }

            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String r1 = rowsM1.getText().toString();
                String c1 = colsM1.getText().toString();
                int rowsMatrix1 = Integer.parseInt(r1);
                int colsMatrix1 = Integer.parseInt(c1);

                if(operationComboBox.getSelectedItem().toString() == "Addition"){
                    DataMatrices dataMatrices =  new DataMatrices(rowsMatrix1,colsMatrix1 , "+", rowsMatrix1,colsMatrix1,socket);
                    dataMatrices.setVisile(true);
                    setVisible(false);
                }
                else{
                    String c2 = colsM2.getText().toString();
                    int colsMatrix2 = Integer.parseInt(c2);
                    DataMatrices dataMatrices =  new DataMatrices(rowsMatrix1,colsMatrix1 , "*", rowsMatrix1,colsMatrix2,socket);
                    dataMatrices.setVisile(true);
                    setVisible(false);
                }

            }
        });

        // Add elements to panel
        allInfo.add(new JLabel("Matrix information : "));
        allInfo.add(operationComboBox);
        allInfo.add(new JLabel("Matrix A:"));
        allInfo.add(infoMatrice1);
        allInfo.add(new JLabel("Matrix B:"));

        allInfo.add(infoMatrice2);
        allInfo.add(btnPanel);
        allInfo.setBorder(new EmptyBorder(10, 30, 10, 30));

        jf.setContentPane(allInfo);
        jf.setTitle("Matrix Calculation");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setSize(600,600);
        jf.setVisible(true);

    }

    public void setVisible(boolean isVisible){
        jf.setVisible(isVisible);
    }

    public void acceptJustNumber(JTextField jTextField){
        jTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume(); // Ignore les caractères non numériques
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Non utilisé, mais requis par l'interface
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Non utilisé, mais requis par l'interface
            }
        });
    }

    public void cloneOneInTwo(JTextField jTextField1,JTextField jTextField2){
        jTextField1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume(); // Ignore les caractères non numériques
                }
                jTextField2.setText(jTextField1.getText());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Non utilisé, mais requis par l'interface
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Non utilisé, mais requis par l'interface
            }
        });
    }

    public void clearData(){
        colsM1.setText("");
        colsM2.setText("");
        rowsM1.setText("");
        rowsM2.setText("");
    }

}

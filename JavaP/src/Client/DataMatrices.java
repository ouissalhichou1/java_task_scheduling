package Client;




import MainServer.MatrixTask;
import Ressources.Matrice;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class DataMatrices {
    private JFrame jFrame ;
    private JPanel allInfo, infoMatrice1, infoMatrice2, btnPanel;
    private JTextField [][] matrix,matrix2;
    private JButton btnSubmited;

    private Matrice m1, m2;
    private Socket socket;


    public DataMatrices(int l1, int c1, String operation, int l2, int c2, Socket socket){
        this.socket = socket;
        jFrame = new JFrame();
        allInfo = new JPanel(new GridLayout( 6, 1));
        matrix = new JTextField[l1][c1];
        matrix2 = new JTextField[l1][c1];

        infoMatrice1 = new JPanel(new GridLayout( l1, c1));
        for(int i=0;i<l1;i++){
            for(int j=0;j<c1;j++){
                // Création de JTextField pour chaque élément de la matrice
                matrix[i][j] = new JTextField();
                infoMatrice1.add(matrix[i][j]);
            }
        }

        infoMatrice2 = new JPanel(new GridLayout( l2, c2));
        for(int i=0;i<l2;i++){
            for(int j=0;j<c2;j++){
                // Création de JTextField pour chaque élément de la matrice
                matrix2[i][j] = new JTextField();
                infoMatrice2.add(matrix2[i][j]);
            }
        }
        btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        btnSubmited = new JButton("Calculate");
        btnSubmited.setBackground(Color.decode("#B3B0CD"));
        btnSubmited.setPreferredSize(new Dimension(100, 40));


        btnPanel.add(btnSubmited);

        allInfo.add(new JLabel("Enter information for matrix A :"));
        allInfo.add(infoMatrice1);
        allInfo.add(new JLabel(operation,SwingConstants.CENTER));
        allInfo.add(new JLabel("Enter information for matrix B :"));
        allInfo.add(infoMatrice2);

        allInfo.add(btnPanel);

        allInfo.setBorder(new EmptyBorder(20, 70, 10, 70));

        jFrame.setContentPane(allInfo);
        jFrame.setTitle("Matrix Calculation");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        jFrame.setResizable(false);
        jFrame.setSize(600,600);
//        jFrame.setVisible(true);

        btnSubmited.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                m1 = new Matrice(l1,c1);
                for(int i=0;i<l1;i++){
                    for(int j=0;j<c1;j++){
                        int v = Integer.valueOf(matrix[i][j].getText().toString());
                        m1.setElement(i,j,v);
                    }
                }
                m2 = new Matrice(l1,c1);
                for(int i=0;i<l2;i++){
                    for(int j=0;j<c2;j++){
                        int v = Integer.valueOf(matrix2[i][j].getText().toString());
                        m2.setElement(i,j,v);
                    }
                }
                String op = "";
                if(operation.equals("+")){
                    op = "Addition";
                }else{
                    op = "Multiplication";
                }

                MatrixTask matrixTask = new MatrixTask(m1,op,m2);

                sendTaskToServer(matrixTask);

            }
        });

    }

    public void setVisile(boolean isVisible){
        jFrame.setVisible(isVisible);
    }
    public void sendTaskToServer(MatrixTask m){
        try{
//            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
//            pw.println("hhhhh");
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(m);

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Matrice m1 = (Matrice) objectInputStream.readObject();
            ResultMatrice resultMatrice = new ResultMatrice(m1);
            resultMatrice.setVisible(true);
            setVisile(false);
            socket.close();
        }catch(Exception e){
            System.out.println(e.getMessage());

        }
    }





}

package Client;



import MainServer.MatrixTask;
import Ressources.Matrice;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultMatrice {
    JFrame jFrame ;
    JPanel main_panel, title_panel, matrice_panel,btnPanel;


    public ResultMatrice(Matrice matrice){
        jFrame = new JFrame();
        main_panel = new JPanel(new GridLayout(3, 1,0,10));

        // Title
        title_panel = new JPanel(new GridLayout(1, 1));
        title_panel.add(new JLabel("The result is : "));

        //matrice
        matrice_panel = new JPanel(new GridLayout(matrice.getNombreLignes(), matrice.getNombreColonnes()));

        for(int i=0;i<matrice.getNombreLignes();i++){
            for(int j=0;j<matrice.getNombreColonnes();j++){
                matrice_panel.add(new JLabel(String.valueOf(matrice.getElement(i,j)),SwingConstants.CENTER));
            }
        }
        matrice_panel.setBackground(Color.decode("#B3B0CD"));

        //Button
        JButton submitButton = new JButton("Ok");
        submitButton.setBackground(Color.decode("#B3B0CD"));
        submitButton.setPreferredSize(new Dimension(100, 40));
        btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(submitButton);

        main_panel.add(title_panel);
        main_panel.add(matrice_panel);
        main_panel.add(btnPanel);

        main_panel.setBorder(new EmptyBorder(20, 70, 20, 70));



        jFrame.setContentPane(main_panel);
        jFrame.setTitle("Matrix Calculation");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setResizable(false);
        jFrame.setSize(600,600);
        jFrame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               setVisible(false);

            }
        });



    }

    public void setVisible(boolean isVisible){
        jFrame.setVisible(isVisible);
    }
}


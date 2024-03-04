package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class ChooseTreatment  {
    private JFrame jFrame = new JFrame();
    private JPanel mainPanel, panelButton1,panelButton2;

    private JLabel titleLabel;
    private JButton matriceBtn;
    private JButton imageBtn;
    private Socket socket;
    static int MainServer_port;
    static String MainServer_host;

    public ChooseTreatment(String[] args){
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
        try{
            socket = new Socket(MainServer_host,MainServer_port);
        }catch(Exception e){};

        mainPanel = new JPanel(new GridLayout( 3, 1,0,10));
        titleLabel = new JLabel("Your treatment will be about :");
        matriceBtn = new JButton("Matrices");
        matriceBtn.setPreferredSize(new Dimension(100, 40));
        matriceBtn.setBackground(Color.decode("#B3B0CD"));
        panelButton1 = new JPanel();
        panelButton1.add(matriceBtn);
        imageBtn = new JButton("Images");
        imageBtn.setPreferredSize(new Dimension(100, 40));
        imageBtn.setBackground(Color.decode("#B3B0CD"));
        panelButton2 = new JPanel();
        panelButton2.add(imageBtn);

        imageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer("Images");
                try{socket.close();}catch(Exception ee){}
                new ImageProccesing(args);
                setVisible(false);
            }
        });
        matriceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer("Matrices");
                MatriceInfo matriceInfo = new MatriceInfo(socket);
                matriceInfo.setVisible(true);
                setVisible(false);
            }
        });

        mainPanel.add(titleLabel);
        mainPanel.add(panelButton1);
        mainPanel.add(panelButton2);
        mainPanel.setBorder(new EmptyBorder(20, 70, 20, 70));


        jFrame.setContentPane(mainPanel);
        jFrame.setTitle("App");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(500,500);
    }

    public void setVisible(boolean isVisible){
        jFrame.setVisible(isVisible);
    }

    public void sendMessageToServer(String message){
        try{
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
            pw.println(message);
        }
        catch(Exception ej){}
    }

}

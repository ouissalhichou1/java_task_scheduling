package MainServer;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Ressources.Matrice;
import RessourcesForRMI.SlaveDataList;
import Slave.Slave2;

public class MainServer extends Thread {


    private static Executor executor;
    private static TaskQueue taskQueue;
    private static int TaskID=100;

    static int MainServer_port;
    static String MainServer_host;
    public static void main(String[] args) {

        /*
         * Read Proprieties from file
         */
        Properties prop=new Properties();
        FileInputStream ip;
        //Par defaut
        String FileConfiguration= "cfgMainServer.properties";
        if(args.length>0)
            FileConfiguration = args[0];
        try {
            ip = new FileInputStream(FileConfiguration);
            prop.load(ip);
        } catch (Exception e2) {
            System.exit(0);
        }



        /*
         * Run the socket of Slave
         */
        MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
        MainServer_host = prop.getProperty("MainServer.host");
        // Creating an object of ServerSocket class
        // in the main() method for socket connection

        /*
         * *
         * GET SLaveS
         */
        getSelvers(prop);
        System.out.println("Number of Slaves :"+ SlaveDataList.ListSlaves.size());


        ServerSocket ss;
        try {
            ServerSocket sp=new ServerSocket(MainServer_port);

            // !Listen to client

                // Accept the socket from client
                Socket s = sp.accept();
                InputStream is = s.getInputStream(); // pour accepter un octe
                InputStreamReader isr =  new InputStreamReader(is); // pour accepter un int
                BufferedReader br = new BufferedReader(isr); // pour accepter une chaine de caracteres
                String message = br.readLine();
                System.out.println(message);

                if (message.equals("Matrices")) {
                    new Slave2(s).start();
                }else{
                    sp.close();
                    ss = new ServerSocket(MainServer_port);
                    executor = Executors.newFixedThreadPool(10);
                    taskQueue = new TaskQueue();

                    // ? Make it nonblocking
                    ss.setReuseAddress(true);
                    ss.setSoTimeout(0);

                    System.out.println("Started at port "+MainServer_port);

                    // !Lunch Mini Workers Threads executing the Queue
                    for (int i = 0; i < 7; i++) {
                        executor.execute(new Worker(taskQueue));
                    }
                    while (true){
                        Socket soc = ss.accept();
                        System.out.println("+("+TaskID+") : New Task in queue");
                        Task newTask = new Task(soc,TaskID++);
                        taskQueue.add(newTask);
                    }



//                System.out.println("+("+TaskID+") : New Task in queue");
//                Task newTask = new Task(soc,TaskID++);
//                taskQueue.add(newTask);
            }

        } catch (Exception e1) {
        }
    }



    public static int getSelvers(Properties prop) {

        String hostSlv = prop.getProperty("Slave1.host");
        String portSlv = prop.getProperty("Slave1.port");
        int i=1;
        while(hostSlv != null){
            SlaveDataList.addSlave("rmi://"+hostSlv+":"+portSlv+ "/Slave");
            i++;

            hostSlv = prop.getProperty("Slave" +i+".host");
            portSlv = prop.getProperty("Slave" +i+".port");
        }
        // listSlevers.add(new ListSlevers("100.100.33.150", 1111));
        return i-1;
    }
}

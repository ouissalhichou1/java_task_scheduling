package MainServer;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static int MainServer_port;
    private static String MainServer_host;
    private static ExecutorService executor;
    private static TaskQueue taskQueue;
    private static int TaskID = 0;

    public static void main(String[] args) {
        // Read properties from file
        Properties prop = new Properties();
        FileInputStream ip;
        // Default configuration file
        String FileConfiguration = "C:\\xampp\\htdocs\\GitHub\\java_task_scheduling\\cfgMainServer.properties";
        if (args.length > 0)
            FileConfiguration = args[0];
        try {
            ip = new FileInputStream(FileConfiguration);
            prop.load(ip);
            ip.close(); // Close the FileInputStream
        } catch (Exception e2) {
            e2.printStackTrace(); // Log the exception
            System.exit(1); // Exit with error code
        }

        // Initialize server properties
        MainServer_port = Integer.parseInt(prop.getProperty("MainServer.port"));
        MainServer_host = prop.getProperty("MainServer.host");

        // Initialize server socket
        try (ServerSocket ss = new ServerSocket(MainServer_port)) {
            executor = Executors.newFixedThreadPool(8); // Adjusted thread pool size
            taskQueue = new TaskQueue();

            System.out.println("Started at port " + MainServer_port);

            // Start worker threads
            for (int i = 0; i < 8; i++) {
                executor.execute(new ServerThread(taskQueue));
            }

            // Listen for incoming connections
            while (true) {
                // Accept the socket from client
                Socket soc = ss.accept();
                System.out.println("+(" + TaskID + ") : New Task in queue");

                // Wait for task name from client
                ObjectInputStream dis = new ObjectInputStream(soc.getInputStream());
                String taskName = (String) dis.readObject();
                System.out.println("+(" + TaskID + ") : Task is " + taskName);

                // Create a new task with the received task name and add it to the task queue
                Task newTask = new Task(soc, TaskID++, taskName);
                taskQueue.add(newTask);
            }
        } catch (Exception e1) {
            e1.printStackTrace(); // Log the exception
        }
    }
}

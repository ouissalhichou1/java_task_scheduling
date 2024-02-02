package MainServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread implements Runnable {
    private TaskQueue taskQueue;

    public ServerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task newTask = taskQueue.take();
                Socket soc = newTask.client;
                System.out.println("+(" + (newTask.TaskId) + ") : Entered");

                // Send to client "dispo"
                ObjectOutputStream d = new ObjectOutputStream(soc.getOutputStream());
                d.writeObject("active");

                // Wait for name task
                ObjectInputStream dis = new ObjectInputStream(soc.getInputStream());
                String taskName = (String) dis.readObject();

                System.out.println("+(" + (newTask.TaskId) + ") : Task is " + taskName);
                if (taskName.compareToIgnoreCase("NOISE SALT AND PEPPER") == 0) {
                    // Handle noise salt and pepper task
                } else if (taskName.compareToIgnoreCase("GRAY FILTER") == 0) {
                    // Handle gray filter task
                } else if (taskName.compareToIgnoreCase("SEPIA FILTER") == 0) {
                    // Handle sepia filter task
                } else if (taskName.compareToIgnoreCase("INVERT FILTER") == 0) {
                    // Handle invert filter task
                } else if (taskName.compareToIgnoreCase("BRIGHTNESS FILTER") == 0) {
                    // Handle brightness filter task
                } else if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
                    // Handle convolution filter task
                }

                // Add a small delay to avoid busy waiting
                Thread.sleep(100); // Adjust the sleep time as needed
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // Handle the InterruptedException
            e.printStackTrace();
            // Optionally, you might want to break out of the loop or perform some cleanup here
        }
    }

    public int isDifficult(String taskName, int width, int height) {
        if (taskName.compareToIgnoreCase("CONVOLUTION FILTER") == 0) {
            if (width > 3000 && height > 3000)
                return 4 ;
            else
                return 1;
        } else
            return 0;
    }
}

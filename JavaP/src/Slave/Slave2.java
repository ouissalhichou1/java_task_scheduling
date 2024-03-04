package Slave;

import MainServer.MatrixTask;
import Ressources.Matrice;

import java.io.*;
import java.net.Socket;

public class Slave2 extends Thread{
    private Socket socket;
    public Slave2(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream(); // pour accepter un octe
            ObjectInputStream objectInputStream = new ObjectInputStream(is);
            while(true){
                MatrixTask m1 = (MatrixTask) objectInputStream.readObject();
                m1.execute();
                Matrice cc = m1.getMatrix3();
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(cc);
            }
        }catch (Exception e){
            System.err.print(e);
        }


    }
}

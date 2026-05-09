package org.example.chatroom;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler (Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                //serveris laukia zinute is konkretaus kliento
                Object obj = in.readObject();
                if (obj instanceof String) {
                    String message = (String) obj;
                    System.out.println("New message: " + message);
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        }
    }
}

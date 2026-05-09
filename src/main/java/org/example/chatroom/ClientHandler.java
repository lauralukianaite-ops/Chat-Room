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
                Message msg = (Message) in.readObject();
                Main.broadcast(msg);
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        }
    }
    public void sendMessage(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

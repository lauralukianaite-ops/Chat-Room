package org.example.chatroom;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server's port: 1234");

            while (true) {
                Socket socket = serverSocket.accept();

                // Sukuriame naują klientą aptarnaujančią giją
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(Message msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    public static void sendPrivateMessage(Message msg, String recipientName) {
        for (ClientHandler client : clients) {
            // Čia tau reikės ClientHandler klasėje turėti metodą getUsername()
            if (client.getUsername().equals(recipientName) || client.getUsername().equals(msg.getSender())) {
                client.sendMessage(msg);
            }
        }
    }
}

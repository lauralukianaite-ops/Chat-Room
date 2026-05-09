package org.example.chatroom;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server's port: 1234");

            while (true) {
                Socket socket = serverSocket.accept();

                // Sukuriame naują klientą aptarnaujančią giją
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

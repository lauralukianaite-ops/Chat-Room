package org.example.chatroom;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private static final String FILE_NAME = "messages.txt";
    private static final String ROOMS_FILE = "rooms.txt";
    private static final String USERS_FILE = "users.txt";

    public static void saveMessage(String message){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))){
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRoom(String roomName) {
        List<String> existingRooms = loadRooms();
        if (!existingRooms.contains(roomName)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOMS_FILE, true))) {
                writer.write(roomName);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> loadRooms() {
        List<String> rooms = new ArrayList<>();
        File file = new File(ROOMS_FILE);

        if (!file.exists()) {
            return rooms;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rooms.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static void saveUser(String username) {
        List<String> users = loadUsers();
        if (!users.contains(username)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                writer.write(username);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> loadUsers() {
        List<String> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) users.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

}

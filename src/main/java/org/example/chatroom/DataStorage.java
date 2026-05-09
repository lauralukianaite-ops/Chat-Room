package org.example.chatroom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataStorage {
    private static final String FILE_NAME = "messages.txt";

    public static void saveMessage(String message){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))){
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

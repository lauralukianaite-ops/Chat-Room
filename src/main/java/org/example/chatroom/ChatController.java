package org.example.chatroom;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ChatController {
    @FXML private Label usernameLabel;
    @FXML private Label currentRoomLabel;
    @FXML private ListView<String> roomsList;
    @FXML private ListView<String> usersList;
    @FXML private TextField messageField;
    @FXML private VBox chatContainer;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String user;

    @FXML private TextField newRoomField;

    public void setupChat(String user, Socket s, ObjectOutputStream out, ObjectInputStream in) {
        this.user = user; this.out = out; this.in = in;
        usernameLabel.setText(user);

        roomsList.getItems().clear();
        if (!roomsList.getItems().contains("# bendras")) {
            roomsList.getItems().add("# bendras");
            roomsList.setStyle("-fx-control-inner-background: #2b2b2b; -fx-background-color: #2b2b2b; -fx-selection-bar: #0091b5;");
        }
        currentRoomLabel.setText("# bendras");

        // Gija, kuri laukia žinučių
        new Thread(() -> {
            try {
                while (true) {
                    Message m = (Message) in.readObject();
                    Platform.runLater(() -> {
                        if ("NEW_ROOM".equals(m.getType())) {
                            // Jei tokio kambario dar nėra sąraše, pridedame
                            if (!roomsList.getItems().contains(m.getContent())) {
                                roomsList.getItems().add(m.getContent());
                            }
                        } else if ("CHAT".equals(m.getType())) {
                            System.out.println("Gauta žinutė: " + m.getContent() + " | room: " + m.getRoom() + " | currentRoom: " + currentRoomLabel.getText());
                            if (m.getRoom() != null && m.getRoom().equals(currentRoomLabel.getText())) {
                                appendMessageToUI(m);
                            }
                        }
                    });
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();

        try {
            List<String> lines = Files.readAllLines(Paths.get("messages.txt"));
            for (String line : lines) {
                appendMessageToUI(new Message("", line, "CHAT", currentRoomLabel.getText()));
            }
        } catch (IOException e) {
            System.out.println("No previous chat history");
        }

        roomsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentRoomLabel.setText(newVal);
                chatContainer.getChildren().clear();
                loadRoomHistory(newVal);
            }
        });

        loadRoomHistory("# bendras");
    }

    @FXML
    private void onSendMessage() {
        String text = messageField.getText();

        if (!text.isEmpty()) {
            try {
                Message msg = new Message(user, text, "CHAT", currentRoomLabel.getText());
                out.writeObject(msg);
                out.flush();
                messageField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void appendMessageToUI(Message msg) {
        //Sukuriamas tekstinįs laukąs zinutei
        Label messageLabel = new Label(msg.getSender() + ": " + msg.getContent());

        messageLabel.setStyle(
                "-fx-background-color: #4f545c; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 12; " +
                        "-fx-background-radius: 15; " +
                        "-fx-max-width: 400; "
        );

        messageLabel.setWrapText(true);
        chatContainer.getChildren().add(messageLabel);
        chatContainer.setSpacing(10);
    }

    @FXML
    private void onCreateRoom() {
        String roomName = newRoomField.getText().trim();
        if (!roomName.isEmpty()) {
            if (!roomName.startsWith("#")) roomName = "#" + roomName;

            try {
                Message msg = new Message(user, roomName, "NEW_ROOM", roomName);
                out.writeObject(msg);
                out.flush();
                newRoomField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadRoomHistory(String roomName) {
        try {
            if (Files.exists(Paths.get("messages.txt"))) {
                List<String> lines = Files.readAllLines(Paths.get("messages.txt"));
                for (String line : lines) {
                    // Skaidome eilutę: [0]=kambarys, [1]=siuntėjas, [2]=tekstas
                    String[] parts = line.split(";", 3);
                    if (parts.length == 3 && parts[0].equals(roomName)) {
                        Message historyMsg = new Message(parts[1], parts[2], "CHAT", roomName);
                        // Naudojam Platform.runLater, kad UI neuzstrigtų
                        Platform.runLater(() -> appendMessageToUI(historyMsg));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Nepavyko nuskaityti istorijos failo.");
        }
    }
}

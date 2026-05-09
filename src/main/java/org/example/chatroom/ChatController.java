package org.example.chatroom;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
    private String currentUser;
    private String user;

    public void setupChat(String user, Socket s, ObjectOutputStream out, ObjectInputStream in) {
        this.user = user; this.out = out; this.in = in;
        usernameLabel.setText(user);

        // Gija, kuri laukia žinučių
        new Thread(() -> {
            try {
                while (true) {
                    Message m = (Message) in.readObject();
                    Platform.runLater(() -> chatContainer.getChildren().add(new Label(m.getSender() + ": " + m.getContent())));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    @FXML
    private void onSendMessage() {
        try {
            out.writeObject(new Message(user, messageField.getText(), "CHAT"));
            messageField.clear();
        } catch (Exception e) { e.printStackTrace(); }
    }
}

package org.example.chatroom;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ChatController {
    @FXML private Label usernameLabel;
    @FXML private ListView<String> roomsList;
    @FXML private ListView<String> usersList;

    public void setUsername(String vardas) {
        usernameLabel.setText(vardas);
    }
}

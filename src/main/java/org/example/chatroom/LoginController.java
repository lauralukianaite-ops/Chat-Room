package org.example.chatroom;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private void onLoginButtonClick() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            System.out.println("Enter your name!");
            return;
        }

        try {
            //Jungiamės prie serverio
            Socket socket = new Socket("localhost", 1234);

            //Sukuriami srautai duomenu siuntimui
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Užkraunamas pagrindinįs chato vaizdas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
            Parent root = loader.load();

            //Perduodami duomenys i ChatController
            ChatController chatController = loader.getController();
            chatController.setupChat(username, socket, out, in);

            //Pakeiciamas langas
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Chat - " + username);
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error! Unable to connect to the server!");
        }
    }
}
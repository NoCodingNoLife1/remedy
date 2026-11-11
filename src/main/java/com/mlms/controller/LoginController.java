package com.mlms.controller;

import com.mlms.util.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    public void handleLogin(ActionEvent event) {
        String inputPassword = txtPassword.getText();
        String currentPassword = DatabaseUtil.getAppPassword();

        if (inputPassword != null && inputPassword.equals(currentPassword)) {
            // Login successful, transition to Dashboard
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mlms/fxml/Dashboard.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 1000, 700));
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lblError.setText("Invalid password. Please try again.");
            lblError.setVisible(true);
            txtPassword.clear();
        }
    }
}

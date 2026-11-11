package com.mlms.controller;

import com.mlms.util.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class SettingsController {

    @FXML private PasswordField txtCurrentPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Label lblMessage;

    @FXML
    public void handleUpdatePassword(ActionEvent event) {
        String currentInput = txtCurrentPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        
        String actualCurrent = DatabaseUtil.getAppPassword();

        if (currentInput == null || !currentInput.equals(actualCurrent)) {
            showMessage("Current password is incorrect.", true);
            return;
        }

        if (newPassword == null || !isValidPassword(newPassword)) {
            showMessage("New password must be 8-16 chars long, contain at least 1 symbol and 1 number.", true);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showMessage("New password and confirmation do not match.", true);
            return;
        }

        DatabaseUtil.updateAppPassword(newPassword);
        showMessage("Password successfully updated!", false);
        
        txtCurrentPassword.clear();
        txtNewPassword.clear();
        txtConfirmPassword.clear();
    }

    private void showMessage(String msg, boolean isError) {
        lblMessage.setText(msg);
        lblMessage.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        lblMessage.setVisible(true);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        
        boolean hasNum = false;
        boolean hasSymbol = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNum = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSymbol = true;
            }
        }
        
        return hasNum && hasSymbol;
    }
}

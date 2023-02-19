package com.controllers;

import com.utils.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.window.Window;

import java.sql.SQLException;

public class Login extends Controller{
    private String validUsername, validPassword, inputUsername, inputPassword;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void login(ActionEvent actionEvent) {
        if (validateCredentials(actionEvent)) {
            Stage stage = Window.getParentWindow(actionEvent);
            Window.changeScene(stage, "customer-menu.fxml", "Scheduling Management");
        }
        else openNotifyWindow("Username or password incorrect", actionEvent);
    }
    @FXML
    private boolean validateCredentials(ActionEvent actionEvent) {
        inputUsername = userNameField.getText();
        inputPassword = passwordField.getText();
        if (validateUsername() && validatePassword()) {
            actionEvent.consume();
            return true;
        }
        actionEvent.consume();
        return false;
    }
    private boolean validateUsername() {
        validUsername = Query.getUsername(inputUsername);
        if (validUsername != null) {
            if (validUsername.equals(inputUsername)) return true;
        }
        return false;
    }
    private boolean validatePassword() {
        validPassword = Query.getPassword(inputUsername);
        if (validPassword.equals(inputPassword)) return true;
        else return false;
    }
}

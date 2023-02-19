package com.controllers;

import com.easyschedule.Instance;
import com.people.User;
import com.utils.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.window.Window;

import java.sql.SQLException;

public class Login extends Controller{
    private User user;
    private String validUsername, validPassword, inputUsername, inputPassword;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void login(ActionEvent actionEvent) {
        if (validateCredentials(actionEvent)) {
            Instance.setActiveUser(user);
            Stage stage = Window.getParentWindow(actionEvent);
            Window.changeScene(stage, "main-menu.fxml", "Scheduling Management");
        }
        else openNotifyWindow("Username or password incorrect", actionEvent);
    }
    @FXML
    private boolean validateCredentials(ActionEvent actionEvent) {
        inputUsername = userNameField.getText();
        inputPassword = passwordField.getText();
        user = Query.getUser(inputUsername);
        if (validateUsername() && validatePassword()) {
            actionEvent.consume();
            return true;
        }
        actionEvent.consume();
        return false;
    }
    private boolean validateUsername() {
        if (user.getUsername() != null) {
            if (user.getUsername().equals(inputUsername)) return true;
        }
        return false;
    }
    private boolean validatePassword() {
        if (user.getPassword().equals(inputPassword)) return true;
        else return false;
    }
}

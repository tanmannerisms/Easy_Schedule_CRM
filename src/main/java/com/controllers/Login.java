package com.controllers;

import com.utils.LoginQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
            openNotifyWindow("Creds are correct", actionEvent);
        }
        else openNotifyWindow("Username or password incorrect", actionEvent);
    }
    @FXML
    private boolean validateCredentials(ActionEvent actionEvent) {
        inputUsername = userNameField.getText();
        inputPassword = passwordField.getText();
        if (validateUsername() && validatePassword()) {
            return true;
        }
        else return false;
    }
    private boolean validateUsername() {
        try {
            validUsername = LoginQuery.getUsername(inputUsername);
            if (validUsername != null) {
                if (validUsername.equals(inputUsername)) return true;
            }
            return false;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    private boolean validatePassword() {
        try {
            validPassword = LoginQuery.getPassword(inputUsername);
            if (validPassword.equals(inputPassword)) return true;
            else return false;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

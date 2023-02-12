package com.controllers;

import com.utils.LoginQuery;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Login extends Controller{
    private String username, password;
    @FXML
    private TextField userNameField, passwordField;
    @FXML
    private boolean validateCreds() {
        username = userNameField.getText();
        password = passwordField.getText();
        if (password.equals(LoginQuery.getPassword(username))) {
            openNotifyWindow("Password is correct");
            return true;
        }
        return false;
    }
}

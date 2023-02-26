package com.controllers;

import com.easyschedule.Instance;
import com.people.User;
import com.utils.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.window.Window;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login extends Controller implements Initializable {
    private User user;
    private String validUsername, validPassword, inputUsername, inputPassword;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label tzLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tzLabel.setText(tzLabel.getText() + Instance.SYSTEMZONEID);
    }
    @FXML
    private void login(ActionEvent actionEvent) {
        if (validateCredentials(actionEvent)) {
            Instance.setActiveUser(user);
            Window.changeScene(actionEvent, "main-menu.fxml", "Main Menu");
        }
        else openNotifyWindow("Username or password incorrect", actionEvent);
    }
    @FXML
    private boolean validateCredentials(ActionEvent actionEvent) {
        inputUsername = userNameField.getText();
        inputPassword = passwordField.getText();
        queryUser();
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
    private void queryUser() {
//        String condition =  + inputUsername;
        ResultSet results = Query.selectConditional(
                "User_Id, User_Name, Password",
                "users",
                "User_Name = ",
                inputUsername
        );
        try {
            while (results.next()) {
                user = new User(
                        results.getInt(1),
                        results.getString(2),
                        results.getString(3)
                );
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

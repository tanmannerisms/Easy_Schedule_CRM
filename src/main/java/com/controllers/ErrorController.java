package com.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController extends Controller implements Initializable {
    @FXML
    private Text errorTextField;
    private String errorMessage = "Error! Please try again.";
    public ErrorController(){
        super();
    }

    /**
     * Sets the standard error message on initialization.
     *
     * @param url not used.
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setErrorTextField(errorMessage);
    }

    /**
     * Sets the error message of the window after initialization.
     *
     * @param text the error message to set.
     * @see ErrorWindow#ErrorWindow(String)  ErrorWindow
     * @see ErrorWindow#ErrorWindow(Exception)  ErrorWindow
     */
    public void setErrorTextField(String text) {
        errorMessage = text;
        errorTextField.setText(errorMessage);
    }
}
package com.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;
import com.easyschedule.Instance;

public class Error extends Controller implements Initializable {
    @FXML
    private Text errorTextField;
    private String errorMessage = "Error! Please try again.";
    private ResourceBundle resourceBundle;
    public Error(){
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
        this.resourceBundle = resourceBundle;
        setErrorTextField(errorMessage);
    }

    /**
     * Sets the error message of the window after initialization.
     *
     * @param text the error message to set.
     * @see com.window.ErrorWindow#ErrorWindow(String)  ErrorWindow
     * @see com.window.ErrorWindow#ErrorWindow(Exception)  ErrorWindow
     */
    public void setErrorTextField(String text) {
        try {
            errorMessage = resourceBundle.getString(text);
        }
        catch (NullPointerException ignored) {
            System.out.println("Unable to find value associated with key " + text);
            errorMessage = text;
        }
        errorTextField.setText(errorMessage);
    }
}
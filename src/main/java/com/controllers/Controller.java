package com.controllers;

import com.window.ErrorWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {


    /**
     * Method to open up a new window with an error message
     *
     * @param e the exception that is passed in from a catch statement.
     */
    protected void openErrorWindow(Exception e, ActionEvent actionEvent) {
        ErrorWindow errorWindow = new ErrorWindow(e);
        errorWindow.showWindowAndWait(actionEvent);
    }


    /**
     * Method used to open a notify window that has multiple messages.
     */
    protected void openNotifyWindow(ActionEvent actionEvent, String ... message) {
        ErrorWindow notifyWindow = new ErrorWindow(message);
        notifyWindow.showWindowAndWait(actionEvent);
    }

    /**
     * Method used to close the active window
     *
     * @param actionEvent is used to gather the information needed to close the window.
     */
    @FXML
    protected void closeWindow(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        actionEvent.consume();
    }
}

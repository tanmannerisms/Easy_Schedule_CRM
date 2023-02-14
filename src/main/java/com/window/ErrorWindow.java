package com.window;

import com.controllers.Error;
import javafx.event.ActionEvent;
import javafx.stage.Modality;

public class ErrorWindow extends Window{

    private static final String title = "Error!";
    private static final String file = "notify.fxml";

    /**
     * The constructor used for instantiating a window to display an error message to the user.
     *
     * @param e the exception that will be used to generate the error message that will be displayed.
     */
    public ErrorWindow(Exception e, ActionEvent actionEvent) {
        super(file, title);
        Error controller = fxmlLoader.getController();
        controller.setErrorTextField(e.getMessage());
        e.printStackTrace();
        stage.initOwner(getParentWindow(actionEvent));
        stage.initModality(Modality.WINDOW_MODAL);
    }

    /**
     * Alternate constructor for generating notification messages. Can be useful for when an error needs to be thrown,
     * but with no legitimate exception
     *
     * @param message the string that will be used when displaying the error window. .
     */
    public ErrorWindow(String message, ActionEvent actionEvent) {
        super(file, title);
        Error controller = fxmlLoader.getController();
        controller.setErrorTextField(message);
        stage.initOwner(getParentWindow(actionEvent));
        stage.initModality(Modality.WINDOW_MODAL);
    }
}
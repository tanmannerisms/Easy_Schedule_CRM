package com.controllers;

import com.window.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MainMenu extends Controller {
    @FXML
    private void onScheduleClick(ActionEvent actionEvent) {
        Stage stage = Window.getParentWindow(actionEvent);
        Window.changeScene(stage, "customer-menu.fxml", "Scheduling Management");
        actionEvent.consume();
    }
    @FXML
    private void onReportsClick(ActionEvent actionEvent) {
        Stage stage = Window.getParentWindow(actionEvent);
        Window.changeScene(stage, "report-menu.fxml", "Scheduling Management");
        actionEvent.consume();
    }
}

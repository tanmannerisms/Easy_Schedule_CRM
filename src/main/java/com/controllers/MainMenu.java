package com.controllers;

import com.window.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenu extends Controller {
    @FXML
    private void onScheduleClick(ActionEvent actionEvent) {
        Window.changeScene(actionEvent, "customer-menu.fxml", "Scheduling Management");
    }
    @FXML
    private void onReportsClick(ActionEvent actionEvent) {
        Window.changeScene(actionEvent, "report-menu.fxml", "Scheduling Management");
    }
}

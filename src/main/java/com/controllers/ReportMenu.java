package com.controllers;

import com.window.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportMenu extends Controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void onAppointmentsClick(ActionEvent actionEvent) {

    }
    @FXML
    private void onScheduleClick(ActionEvent actionEvent) {
        Window contactSchedule = new Window("contact-schedule.fxml", "Schedule");
        contactSchedule.showWindow();
    }
    @FXML
    private void onFreeTimeClick(ActionEvent actionEvent) {

    }
    @FXML
    private void onBackClick(ActionEvent actionEvent) {
        Window.changeScene(actionEvent, "main-menu.fxml", "Main Menu");
    }
}

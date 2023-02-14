package com.controllers;

import com.window.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenu extends Controller {

    @FXML
    private void openCustomers(ActionEvent actionEvent) {
        Window customers = new Window("customer-menu.fxml", "Customer Management");
        customers.showWindowAndWait(actionEvent);
    }
    @FXML
    private void openCalendar(ActionEvent actionEvent) {
        Window calendar = new Window("calendar.fxml", "Calendar");
        calendar.showWindowAndWait(actionEvent);
    }
}

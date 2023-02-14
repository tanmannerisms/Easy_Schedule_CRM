package com.controllers;

import com.window.Window;
import javafx.fxml.FXML;

public class MainMenu extends Controller {

    @FXML
    private void openCustomers() {
        Window customers = new Window("customer-menu.fxml", "Customer Management");
        customers.showWindowAndWait();
    }
    @FXML
    private void openCalendar() {
        Window calendar = new Window("calendar.fxml", "Calendar");
        calendar.showWindowAndWait();
    }
}

package com.controllers;

import com.easyschedule.Appointment;
import com.people.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class Appointments extends Controller implements Initializable {
    private Customer customer;
    @FXML
    private Label customerLabel;
    public TableColumn<Appointment, Integer> idColumn, customerColumn, userColumn;
    public TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    public TableColumn<Appointment, Date> startColumn, endColumn;
    public TableView appointmentsTable;
    public TabPane tabPane;
    public Tab allAppointments, monthAppointments, weekAppointments;
    public TextField appointmentSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    protected void setCustomer(Customer customer) {
        this.customer = customer;
        customerLabel.setText("Customer: " + customer.getName());
    }
    @FXML
    private void  onSearchClick() {

    }
    @FXML
    private void onAddClick() {

    }
    @FXML
    private void onModifyClick() {

    }
    @FXML
    private void onDeleteClick() {

    }
    @FXML
    private void updateTable() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.equals(allAppointments)) {
            //Get all associated appointments
        }
        if (selectedTab.equals(monthAppointments)) {
            // Get all associated appointments 31 days after today.
        }
        if (selectedTab.equals(weekAppointments)) {
            // Get all associated appointments 7 days after today.
        }
    }
}

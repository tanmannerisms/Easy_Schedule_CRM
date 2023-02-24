package com.controllers;

import com.easyschedule.Appointment;
import com.people.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;

public class Appointments extends Controller implements Initializable {
    private Customer customer;
    @FXML
    private Label customerLabel;
    @FXML
    private TableColumn<Appointment, Integer> idColumn, customerColumn, userColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn, endColumn;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab allAppointments, monthAppointments, weekAppointments;
    @FXML
    private TextField appointmentSearchField;

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

        ZonedDateTime now = ZonedDateTime.now();
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab.equals(allAppointments)) {
            //Get all associated appointments
            appointmentsTable.setItems(customer.getAssociatedAppointments());
        }
        if (selectedTab.equals(monthAppointments)) {
            // Get all associated appointments one Month after today.

            ZonedDateTime before = now.plusMonths(1);
            getAppointments(now, before);
        }
        if (selectedTab.equals(weekAppointments)) {
            // Get all associated appointments 7 days after today.
        }
    }
    private ObservableList<Appointment> getAppointments(ZonedDateTime after, ZonedDateTime before) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();


        for (Appointment appointment : customer.getAssociatedAppointments()){
            if () {
                returnList.add(appointment);
            }
        }

        return returnList;
    }
}

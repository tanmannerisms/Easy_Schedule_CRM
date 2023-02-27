package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Customer;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

public class AppointmentsReport extends Controller implements Initializable {
    private final ObservableList<Customer> CUSTOMERS = Instance.getAllCustomers();
    private ObservableList<Appointment> tableAppointments = FXCollections.observableArrayList();
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();
    private ZonedDateTime now;
    private Customer customer;
    private Month month;
    private String type;
    @FXML
    private ComboBox<Month> monthSelector;
    @FXML
    private ComboBox<String> typeSelector, customerSelector;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set date to Jan 1 of current year
        LocalDate today = LocalDate.now();
        LocalDate january = LocalDate.ofYearDay(today.getYear(), 1);
        LocalTime time = LocalTime.of(0,0);
        now = ZonedDateTime.of(january,time,Instance.SYSTEMZONEID);

        populateCustomers();
        populateMonths();
        populateTypes();
    }
    private void populateTypes() {
        try {
            ResultSet rs = Query.selectUnique("type", "appointments");
            while (rs.next()) {
                types.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        typeSelector.setItems(types);
    }
    private void populateMonths() {
        ObservableList<Month> months = FXCollections.observableArrayList();
        for (int i = 0; i < 12; i++) {
            months.add(now.getMonth());
            now = now.plusMonths(1);
        }
        monthSelector.setItems(months);
    }
    private void populateCustomers() {
        for (Customer customer : CUSTOMERS) {
            customerNames.add(customer.getId() + " : " + customer.getName());
        }
        customerSelector.setItems(customerNames);
    }
    @FXML
    private void selectType(ActionEvent actionEvent) {
        type = null;
        type = typeSelector.getValue();
        if (monthSelector.getValue() != null && customerSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }
    @FXML
    private void selectMonth(ActionEvent actionEvent) {
        month = null;
        month = monthSelector.getValue();
        if (typeSelector.getValue() != null && customerSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }
    @FXML
    private void selectCustomer(ActionEvent actionEvent) {
        String customerName = customerSelector.getValue();
        int index = customerNames.indexOf(customerName);
        customer = CUSTOMERS.get(index);
        if (monthSelector.getValue() != null || typeSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }
    private void updateTable(ActionEvent actionEvent) {
        ObservableList<Appointment> customerAppointments = Instance.getCustomerAppointments(customer);

/*
        for (Appointment appointment : customerAppointments) {
            if (appointment.getType().equals(type) && appointment.getStartDate().i)
        }
*/
    }
}

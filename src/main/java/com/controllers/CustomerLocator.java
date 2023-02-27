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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CustomerLocator extends Controller implements Initializable {
    private final ObservableList<Customer> CUSTOMERS = Instance.getAllCustomers();
    private ObservableList<Appointment> tableAppointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();
    private ZonedDateTime now;
    private Customer customer;
    private Month month;
    private String type;
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private ComboBox<Month> divisionSelector;
    @FXML
    private ComboBox<String> countrySelector;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn, phoneColumn, addressColumn;
    public CustomerLocator() {
        // Set date to Jan 1 of current year
        LocalDate today = LocalDate.now();
        LocalDate january = LocalDate.ofYearDay(today.getYear(), 1);
        LocalTime time = LocalTime.of(0,0);
        now = ZonedDateTime.of(january,time,Instance.SYSTEMZONEID);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateDivisions();
        populateTypes();

        setTableColumns();
    }
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
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
        countrySelector.setItems(types);
    }
    private void populateDivisions() {
        ObservableList<Month> months = FXCollections.observableArrayList();
        int year = now.getYear();
        for (int i = 0; i < 12; i++) {
            months.add(now.getMonth());
            now = now.plusMonths(1);
        }
        now = now.withYear(year);
        divisionSelector.setItems(months);
    }
    @FXML
    private void selectCountry(ActionEvent actionEvent) {
        type = null;
        type = countrySelector.getValue();
        if (divisionSelector.getValue() != null && customerSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }
    @FXML
    private void selectDivision(ActionEvent actionEvent) {
        month = null;
        month = divisionSelector.getValue();
        if (countrySelector.getValue() != null && customerSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }
    private void updateTable(ActionEvent actionEvent) {
        tableAppointments.clear();
        for (Appointment appointment : customerAppointments) {
            if (
                    appointment.getType().equals(type) &&
                            appointment.getStartDate().isAfter(now.withMonth(month.getValue())) &&
                            appointment.getStartDate().isBefore(now.withMonth(month.getValue() + 1))
            ) {
                tableAppointments.add(appointment);
            }
        }
        customersTable.setItems(tableAppointments);
        actionEvent.consume();
    }
}


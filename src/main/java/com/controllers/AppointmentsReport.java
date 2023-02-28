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
import java.time.*;
import java.util.ResourceBundle;

public class AppointmentsReport extends Controller implements Initializable {
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
    private TableView<Appointment> appointmentsTable;
    @FXML
    private ComboBox<Month> monthSelector;
    @FXML
    private ComboBox<String> typeSelector, customerSelector;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn, typeColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn, endColumn;

    /**
     * Constructor used to set a ZonedDateTime at the start of today.
     */
    public AppointmentsReport() {
        // Set date to Jan 1 of current year
        LocalDate today = LocalDate.now();
        LocalDate january = LocalDate.ofYearDay(today.getYear(), 1);
        LocalTime time = LocalTime.of(0,0);
        now = ZonedDateTime.of(january,time,Instance.SYSTEMZONEID);
    }

    /**
     * Calls the methods to populate ComboBoxes and establish the table column getters.
     * @param url passed in from the FXMLLoader in Window.java
     * @param resourceBundle passed in from the FXMLLoader in Window.java
     * @see #populateTypes()
     * @see #populateCustomers()
     * @see #populateMonths()
     * @see #setTableColumns()
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCustomers();
        populateMonths();
        populateTypes();

        setTableColumns();
    }

    /**
     * Sets the getters for the table columns in the appointments table.
     */
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("FormattedStartDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("FormattedEndDate"));
    }

    /**
     * Queries the appointments table to get all the unique types. Adds the returned values to the types list.
     */
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

    /**
     * Sets the values of the month ComboBox to each month of the year.
     */
    private void populateMonths() {
        ObservableList<Month> months = FXCollections.observableArrayList();
        int year = now.getYear();
        for (int i = 0; i < 12; i++) {
            months.add(now.getMonth());
            now = now.plusMonths(1);
        }
        now = now.withYear(year);
        monthSelector.setItems(months);
    }

    /**
     * Sets the values of the customer ComboBox to their IDs and names.
     */
    private void populateCustomers() {
        for (Customer customer : CUSTOMERS) {
            customerNames.add(customer.getId() + " : " + customer.getName());
        }
        customerSelector.setItems(customerNames);
    }

    /**
     * Gets the selected value from the Type ComboBox and updates the table if the other ComboBoxes are not null.
     * @param actionEvent the action event fired by the button.
     * @see #updateTable(ActionEvent)
     */
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

    /**
     * Gets the selected value from the Month ComboBox and updates the table if the other ComboBoxes are not null.
     * @param actionEvent the action event fired by the button.
     * @see #updateTable(ActionEvent)
     */
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

    /**
     * Gets the selected value of the Customer ComboBox and updates the table if the other ComboBoxes are not null.
     * @param actionEvent the action event fired by the button.
     * @see #updateTable(ActionEvent)
     */
    @FXML
    private void selectCustomer(ActionEvent actionEvent) {
        String customerName = customerSelector.getValue();
        int index = customerNames.indexOf(customerName);
        customer = CUSTOMERS.get(index);
        customerAppointments = customer.getAssociatedAppointments();
        if (monthSelector.getValue() != null || typeSelector.getValue() != null) {
            updateTable(actionEvent);
            return;
        }
        actionEvent.consume();
    }

    /**
     * Updates the table with a list of appointments matching the values selected in the ComboBoxes
     * @param actionEvent the action event fired by the button.
     */
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
        appointmentsTable.setItems(tableAppointments);
        actionEvent.consume();
    }
}

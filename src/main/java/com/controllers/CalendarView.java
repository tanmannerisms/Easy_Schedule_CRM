package com.controllers;

import com.easyschedule.Appointment;
import com.people.Customer;
import com.window.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class CalendarView extends Controller implements Initializable {
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
        setTableColumns();
    }
    private void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("ContactId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
    }
    protected void setCustomer(Customer customer) {
        this.customer = customer;
        customerLabel.setText("Customer: " + customer.getName());
        updateTable();
    }
    @FXML
    private void  onSearchClick(ActionEvent actionEvent) {

    }
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        Window addAppointment = new Window("appointment.fxml", "Add Appointment");
        AppointmentManagement controller = (AppointmentManagement) addAppointment.getController();
        controller.setCustomer(customer);
        addAppointment.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {
        Window modifyAppointment = new Window("appointment.fxml", "Modify Appointment");
        AppointmentManagement controller = (AppointmentManagement) modifyAppointment.getController();
        controller.setCustomer(customer);
        controller.setAppointment(appointmentsTable.getSelectionModel().getSelectedItem());
        modifyAppointment.showWindowAndWait(actionEvent);
    }
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {

    }
    @FXML
    private void updateTable() {

        LocalDateTime now = LocalDateTime.now();
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab.equals(allAppointments)) {
            //Get all associated appointments
            appointmentsTable.setItems(customer.getAssociatedAppointments());
        }
        if (selectedTab.equals(monthAppointments)) {
            // Get all associated appointments one Month after today.

            LocalDateTime before = now.plusMonths(1);
            appointmentsTable.setItems(getAppointments(now, before));
        }
        if (selectedTab.equals(weekAppointments)) {
            // Get all associated appointments 7 days after today.
            LocalDateTime before = now.plusWeeks(1);
            appointmentsTable.setItems(getAppointments(now, before));
        }
    }
    private ObservableList<Appointment> getAppointments(LocalDateTime after, LocalDateTime before) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();

        for (Appointment appointment : customer.getAssociatedAppointments()){
            LocalDateTime appointmentDate = appointment.getStartDate();
            if (appointmentDate.isAfter(after) && appointmentDate.isBefore(before)) {
                returnList.add(appointment);
            }
        }
        return returnList;
    }
}

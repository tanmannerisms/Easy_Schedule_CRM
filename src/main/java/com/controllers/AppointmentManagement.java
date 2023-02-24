package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Contact;
import com.people.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.util.ResourceBundle;

public class AppointmentManagement extends Controller implements Initializable {
    @FXML private Label title;
    @FXML
    private TextField idField, customerField, titleField, descriptionField, locationField, typeField;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private ComboBox<Contact> contactSelector;
    @FXML
    private ComboBox<Integer> startHourSelector, startMinuteSelector, endHourSelector, endMinuteSelector;
    private ObservableList<Integer> hours, minutes;
    private Appointment appointment;
    private boolean appointmentImported;
    private Customer customer;

    public AppointmentManagement() {
        hours = FXCollections.observableArrayList();
        minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) {
            Integer fifteenth = i * 15;
            minutes.add(fifteenth);
        }
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startHourSelector.setItems(hours);
        endHourSelector.setItems(hours);
        startMinuteSelector.setItems(minutes);
        endMinuteSelector.setItems(minutes);
        appointmentImported = false;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        appointmentImported = true;
    }
    @FXML
    private void onSaveClick(ActionEvent actionEvent) {
        LocalDateTime startDateTime = createDateTime(
                startDatePicker.getValue(),
                startHourSelector.getSelectionModel().getSelectedItem(),
                startMinuteSelector.getSelectionModel().getSelectedItem());
        LocalDateTime endDateTime = createDateTime(
                endDatePicker.getValue(),
                endHourSelector.getValue(),
                endMinuteSelector.getValue()
        );
        if (appointmentImported) {
            appointment.setTitle(titleField.getText());
            appointment.setType(typeField.getText());
            appointment.setLocation(locationField.getText());
            appointment.setDescription(descriptionField.getText());
            appointment.setUserId(Instance.getActiveUser().getId());
            appointment.setContactId(contactSelector.getValue().getId());
            appointment.setStartDate(startDateTime);
            appointment.setEndDate(endDateTime);
        }
        else {
            Appointment newAppointment = new Appointment(
                    Instance.getActiveUser().getId(),
                    customer.getId(),
                    contactSelector.getValue().getId(),
                    titleField.getText(),
                    descriptionField.getText(),
                    locationField.getText(),
                    typeField.getText(),
                    startDateTime,
                    endDateTime
            );
            Instance.addAppointment(newAppointment);
        }
    }
    private LocalDateTime createDateTime(LocalDate date, Integer hours, Integer minutes) {
        LocalDateTime time = date.atStartOfDay();
        time = time.plusHours(hours).plusMinutes(minutes);
        return time;
    }
}

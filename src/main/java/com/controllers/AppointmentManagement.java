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
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.util.ResourceBundle;

public class AppointmentManagement extends Controller implements Initializable {
    @FXML
    private Label title;
    @FXML
    private TextField idField, customerField, titleField, descriptionField, locationField, typeField;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private ComboBox<String> contactSelector;
    @FXML
    private ComboBox<Integer> startHourSelector, startMinuteSelector, endHourSelector, endMinuteSelector;
    private ObservableList<Integer> hours, minutes;
    private ObservableList<String> contactNameList;
    private Appointment appointment;
    private boolean appointmentImported;
    private Customer customer;
    private LocalDate today = LocalDate.now(Instance.SYSTEMZONEID);
    private final ZonedDateTime bussinessHourStart = ZonedDateTime.now();

    public AppointmentManagement() {
        contactNameList = FXCollections.observableArrayList();
        hours = FXCollections.observableArrayList();
        minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) {
            Integer fifteenth = i * 15;
            minutes.add(fifteenth);
        }
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        for (Contact contact : Instance.getAllContacts()) {
            contactNameList.add(contact.getName());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);
        startHourSelector.setItems(hours);
        startHourSelector.setValue(0);
        endHourSelector.setItems(hours);
        endHourSelector.setValue(0);
        startMinuteSelector.setItems(minutes);
        startMinuteSelector.setValue(0);
        endMinuteSelector.setItems(minutes);
        endMinuteSelector.setValue(0);
        contactSelector.setItems(contactNameList);
        appointmentImported = false;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerField.setText(Integer.toString(customer.getId()));
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        appointmentImported = true;
        idField.setText(Integer.toString(appointment.getAppointmentId()));
        contactSelector.setValue(Instance.getContact(appointment.getContactId()).getName());
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        startDatePicker.setValue(getDate(appointment.getStartDate()));
        startHourSelector.setValue(getHours(appointment.getStartDate()));
        startMinuteSelector.setValue(getMinutes(appointment.getStartDate()));
        endDatePicker.setValue(getDate(appointment.getEndDate()));
        endHourSelector.setValue(getHours(appointment.getEndDate()));
        endMinuteSelector.setValue(getMinutes(appointment.getEndDate()));
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
        if (validateFields(actionEvent, startDateTime, endDateTime)) {
            if (appointmentImported) {
                appointment.setTitle(titleField.getText());
                appointment.setType(typeField.getText());
                appointment.setLocation(locationField.getText());
                appointment.setDescription(descriptionField.getText());
                appointment.setUserId(Instance.getActiveUser().getId());
                appointment.setContactId(Instance.getContact(contactSelector.getValue()).getId());
                appointment.setStartDate(startDateTime);
                appointment.setEndDate(endDateTime);
                Instance.updateAppointment(appointment);
            }
            else {
                Appointment newAppointment = new Appointment(
                        Instance.getActiveUser().getId(),
                        customer.getId(),
                        Instance.getContact(contactSelector.getValue()).getId(),
                        titleField.getText(),
                        descriptionField.getText(),
                        locationField.getText(),
                        typeField.getText(),
                        startDateTime,
                        endDateTime
                );
                Instance.addAppointment(newAppointment);
                customer.addAssociatedAppointments(newAppointment);
            }
            closeWindow(actionEvent);
        }
    }
    private boolean validateFields(ActionEvent actionEvent, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (fieldsEmpty()){
            openNotifyWindow("Ensure all fields are filled out and try again", actionEvent);
            return false;
        }
        if (startDateTime.isAfter(endDateTime)) {
            openNotifyWindow("Start date/time must be before end date/time", actionEvent);
            return false;
        }
        if (startDateTime.isBefore(now) || endDateTime.isBefore(now)) {
            openNotifyWindow("Start and End Dates/Times must be in the future.", actionEvent);
            return false;
        }
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        return true;
    }
    private boolean fieldsEmpty() {
        if (
                contactSelector.getValue() == null ||
                        title.getText().isEmpty() ||
                        descriptionField.getText().isEmpty() ||
                        locationField.getText().isEmpty() ||
                        typeField.getText().isEmpty() ||
                        startDatePicker.getValue() == null ||
                        endDatePicker.getValue() == null
        ) {
            return false;
        }
        return true;
    }
    private LocalDateTime createDateTime(LocalDate date, Integer hours, Integer minutes) {
        LocalDateTime time = date.atStartOfDay();
        time = time.plusHours(hours).plusMinutes(minutes);
        return time;
    }
    private LocalDate getDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }
    private Integer getHours(LocalDateTime dateTime) {
        return dateTime.getHour();
    }
    private Integer getMinutes(LocalDateTime dateTime) {
        return dateTime.getMinute();
    }
}

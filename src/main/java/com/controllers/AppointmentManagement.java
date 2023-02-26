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
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.temporal.TemporalAccessor;
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
        LocalDate today = LocalDate.now(Instance.SYSTEMZONEID);

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
        ZonedDateTime startDateTime = createDateTime(
                startDatePicker.getValue(),
                startHourSelector.getSelectionModel().getSelectedItem(),
                startMinuteSelector.getSelectionModel().getSelectedItem());
        ZonedDateTime endDateTime = createDateTime(
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
    private boolean validateFields(ActionEvent actionEvent, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        ZonedDateTime now = ZonedDateTime.now(Instance.SYSTEMZONEID);
        if (fieldsEmpty()){
            openNotifyWindow(actionEvent, "notify.emptyFields");
            return false;
        }
        if (startDateTime.isAfter(endDateTime)) {
            openNotifyWindow(actionEvent, "notify.startBeforeEnd");
            return false;
        }
        if (startDateTime.isBefore(now) || endDateTime.isBefore(now)) {
            openNotifyWindow(actionEvent, "notify.startEndFuture");
            return false;
        }
        if (startDateTime.getDayOfYear() != endDateTime.getDayOfYear()) {
            openNotifyWindow(actionEvent, "notify.startEndSameDay");
            return false;
        }
        if (startDateTime.equals(endDateTime)) {
            openNotifyWindow(actionEvent, "notify.startEndSame");
        }
        if (!inBusinessHours(startDateTime, endDateTime, actionEvent)) {
            return false;
        }
        if (overlappingAppointments(startDateTime, endDateTime, actionEvent)) {
            return false;
        }
        return true;
    }
    private boolean fieldsEmpty() {
        if (
                contactSelector.getValue() == null ||
                        titleField.getText().isEmpty() ||
                        descriptionField.getText().isEmpty() ||
                        locationField.getText().isEmpty() ||
                        typeField.getText().isEmpty() ||
                        startDatePicker.getValue() == null ||
                        endDatePicker.getValue() == null
        ) {
            return true;
        }
        return false;
    }
    private boolean inBusinessHours(ZonedDateTime start, ZonedDateTime end, ActionEvent actionEvent) {
        LocalDate selectedDay = startDatePicker.getValue();
        ZonedDateTime businessHourStart = ZonedDateTime.of(selectedDay, LocalTime.of(8,00),Instance.BUSINESSZONEID);
        ZonedDateTime businessHourEnd = ZonedDateTime.of(selectedDay, LocalTime.of(22,0), Instance.BUSINESSZONEID);
        if (start.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
            start.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
            end.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
            end.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
            (start.isBefore(businessHourStart) || end.isAfter(businessHourEnd))
        ) {
            openNotifyWindow(actionEvent, "notify.businessHours");
            return false;
        }
        return true;
    }
    private boolean overlappingAppointments(ZonedDateTime start, ZonedDateTime end, ActionEvent actionEvent) {
        for (Appointment testAppointment : customer.getAssociatedAppointments()) {
            if (testAppointment.equals(appointment)) {
                continue;
            }
            ZonedDateTime appointmentStart = testAppointment.getStartDate();
            ZonedDateTime appointmentEnd = testAppointment.getEndDate();
            if (
                    start.isEqual(appointmentStart) ||
                            ((start.isAfter(appointmentStart) && end.isBefore(appointmentEnd)) || end.isEqual(appointmentEnd)) ||
                            (start.isBefore(appointmentStart) &&  (end.isAfter(appointmentStart) && end.isBefore(appointmentEnd))) ||
                            (start.isAfter(appointmentStart) && start.isBefore(appointmentEnd) && end.isAfter(appointmentEnd)) ||
                            (start.isBefore(appointmentStart) && end.isAfter(appointmentEnd))
            ) {
                openNotifyWindow(actionEvent,
                        "notify.overlappingAppointment",
                        String.valueOf(testAppointment.getAppointmentId())
                );
                return true;
            }
        }
        return false;
    }
    private ZonedDateTime createDateTime(LocalDate date, Integer hours, Integer minutes) {
        LocalDateTime localDateTime = date.atStartOfDay();
        localDateTime = localDateTime.plusHours(hours).plusMinutes(minutes);

        ZonedDateTime time = ZonedDateTime.of(localDateTime, Instance.SYSTEMZONEID);

        return time;
    }
    private LocalDate getDate(ZonedDateTime dateTime) {
        return dateTime.toLocalDate();
    }
    private Integer getHours(ZonedDateTime dateTime) {
        return dateTime.getHour();
    }
    private Integer getMinutes(ZonedDateTime dateTime) {
        return dateTime.getMinute();
    }
}

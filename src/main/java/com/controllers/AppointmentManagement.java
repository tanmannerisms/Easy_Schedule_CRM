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
    public DatePicker startDatePicker, endDatePicker;
    @FXML
    private ComboBox<String> contactSelector;
    @FXML
    private ComboBox<Integer> startHourSelector, startMinuteSelector, endHourSelector, endMinuteSelector;
    private ObservableList<Integer> hours, minutes;
    private ObservableList<String> contactNameList;
    private Appointment appointment;
    private boolean appointmentImported;
    private Customer customer;

    /**
     * Initializes the contactNameList and sets the values to be used in the comboBoxes.
     */
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

    /**
     * Sets the local time and values of the comboBoxes.
     * @param url set by the FXMLLoader in Window.java
     * @param resourceBundle set by the FXMLLoader in Window.java
     */
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

    /**
     * Used for setting the imported customer. The imported customer should never be null.
     * @param customer the imported customer.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerField.setText(Integer.toString(customer.getId()));
    }

    /**
     * Sets the imported appointment and auto-populates the fields.
     * @param appointment the appointment to import.
     */
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

    /**
     * Triggered by clicking the save button. Validates and records the information from the fields and uses that to 
     * either update the appointment or create a new one, depending on whether an appointment was set before opening this page.
     * @param actionEvent action event passed in by the button click.
     * @see #setAppointment(Appointment) 
     * @see #validateFields(ActionEvent, ZonedDateTime, ZonedDateTime) 
     * @see Instance#addAppointment(Appointment) 
     * @see Instance#updateAppointment(Appointment)
     */
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

    /**
     * Used to validate all the input information on the form. Specifically if any fields are empty and whether the start/end dates are valid.
     * @param actionEvent the action event sent by the save button.
     * @param startDateTime the appointment start Date and Time as set in onSaveClick()
     * @param endDateTime the appointment end Date and Time as set in onSaveClick()
     * @return true if all values are valid
     * @see #fieldsEmpty() 
     * @see #inBusinessHours(ZonedDateTime, ZonedDateTime, ActionEvent) 
     * @see #overlappingAppointments(ZonedDateTime, ZonedDateTime, ActionEvent) 
     */
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
            openNotifyWindow(actionEvent, "notify.businessHours");
            return false;
        }
        if (overlappingAppointments(startDateTime, endDateTime, actionEvent)) {
            return false;
        }
        return true;
    }

    /**
     * Checks to see if any of the fields are empty.
     * @return true if any field empty and false if all fields have data.
     */
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

    /**
     * Checks to see if the dates and times selected are within the business hours of the company.
     * @param start the start date and time set in the form.
     * @param end the end date and time set in the form.
     * @param actionEvent the action event passed in, used to open the error window.
     * @return true if values specified are within business hours and false if not.
     */
    public boolean inBusinessHours(ZonedDateTime start, ZonedDateTime end, ActionEvent actionEvent) {
        LocalDate selectedDay = startDatePicker.getValue();
        ZonedDateTime businessHourStart = ZonedDateTime.of(selectedDay, LocalTime.of(8,0),Instance.BUSINESSZONEID);
        ZonedDateTime businessHourEnd = ZonedDateTime.of(selectedDay, LocalTime.of(22,0), Instance.BUSINESSZONEID);
        if (start.isBefore(businessHourStart) || end.isAfter(businessHourEnd)) {
            return false;
        }
        return true;
    }

    /**
     * Checks to see if the selected start and end times are overlapping other appointment times.
     * @param start the start date and time set in the form.
     * @param end the end date and time set in the form.
     * @param actionEvent the action event passed in, used to open the error window.
     * @return true if overlapping another appointment and false if not.
     */
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

    /**
     * Creates a ZonedDateTime based on the values passed in.
     * @param date the date to set.
     * @param hours the hours to set the time to.
     * @param minutes the minutes to set the time to
     * @return a ZonedDateTime at the specified date and time.
     */
    private ZonedDateTime createDateTime(LocalDate date, Integer hours, Integer minutes) {
        LocalDateTime localDateTime = date.atStartOfDay();
        localDateTime = localDateTime.plusHours(hours).plusMinutes(minutes);

        ZonedDateTime time = ZonedDateTime.of(localDateTime, Instance.SYSTEMZONEID);

        return time;
    }

    /**
     * Gets a LocalDate from a ZonedDateTime.
     * @param dateTime ZonedDateTime to parse.
     * @return the LocalDate from the ZonedDateTime.
     */
    private LocalDate getDate(ZonedDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    /**
     * Gets the hours from a ZonedDateTime.
     * @param dateTime the ZonedDateTime to parse.
     * @return the number of hours from the start of the day from the ZonedDateTime.
     */
    private Integer getHours(ZonedDateTime dateTime) {
        return dateTime.getHour();
    }

    /**
     * Gets the minutes from a ZonedDateTime
     * @param dateTime the ZonedDateTime to parse.
     * @return the number of minutes from the top of the hour stored in the ZonedDateTime
     */
    private Integer getMinutes(ZonedDateTime dateTime) {
        return dateTime.getMinute();
    }
}

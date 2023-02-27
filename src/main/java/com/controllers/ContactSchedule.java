package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class ContactSchedule extends CalendarView implements Initializable {
    private ObservableList<String> nameList = FXCollections.observableArrayList();
    private ObservableList<Contact> contactList;
    private Contact selectedContact;
    @FXML
    private ComboBox<String> contactSelector;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();

        contactList = Instance.getAllContacts();
        String name = null;
        for (Contact contact : contactList) {
            name = contact.getName();
            nameList.add(name);
        }
        contactSelector.setItems(nameList);
    }
    @FXML
    private void updateTable() {
        if (selectedContact == null) {
            return;
        }
        ZonedDateTime now = ZonedDateTime.now(Instance.SYSTEMZONEID);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab.equals(allAppointments)) {
            //Get all associated appointments
            appointmentsTable.setItems(associatedAppointments);
        }
        else if (selectedTab.equals(monthAppointments)) {
            // Get all associated appointments one Month after today.

            ZonedDateTime before = now.plusMonths(1);
            appointmentsTable.setItems(getAppointments(now, before));
        }
        else if (selectedTab.equals(weekAppointments)) {
            // Get all associated appointments 7 days after today.
            ZonedDateTime before = now.plusWeeks(1);
            appointmentsTable.setItems(getAppointments(now, before));
        }
    }
    @FXML
    private void updateContact(ActionEvent actionEvent) {
        String name = contactSelector.getValue();
        selectedContact = contactList.get(nameList.indexOf(name));
        associatedAppointments = FXCollections.observableArrayList();
        for (Appointment appointment : Instance.getAllAppointments()) {
            if (appointment.getContactId() == selectedContact.getId()) {
                associatedAppointments.add(appointment);
            }
        }
        updateTable();
    }
}

package com.controllers;

import com.easyschedule.Instance;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class ContactSchedule extends CalendarView implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @Override
    private void updateTable() {
        ZonedDateTime now = ZonedDateTime.now(Instance.SYSTEMZONEID);
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab.equals(allAppointments)) {
            //Get all associated appointments
            appointmentsTable.setItems(customer.getAssociatedAppointments());
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
}

package com.controllers;

import com.easyschedule.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

public class Appointments extends Controller {
    public TableColumn<Appointment, Integer> idColumn, customerColumn, userColumn;
    public TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    public TableColumn<Appointment, ZonedDateTime>startColumn, endColumn;
    public TableView appointmentsTable;
    public Tab allAppointments, monthAppointments, weekAppointments;
    public TextField appointmentSearchField;

    @FXML
    private void  onSearchClick() {

    }
    @FXML
    private void onAddClick() {

    }
    @FXML
    private void onModifyClick() {

    }
    @FXML
    private void onDeleteClick() {

    }
}

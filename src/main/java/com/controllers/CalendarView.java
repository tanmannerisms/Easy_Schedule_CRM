package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Customer;
import com.window.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
    protected TableColumn<Appointment, Integer> idColumn, customerColumn, userColumn;
    @FXML
    protected TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    @FXML
    protected TableColumn<Appointment, String> startColumn, endColumn;
    @FXML
    protected TableView<Appointment> appointmentsTable;
    protected ObservableList<Appointment> associatedAppointments;
    @FXML
    protected TabPane tabPane;
    @FXML
    protected Tab allAppointments, monthAppointments, weekAppointments;
    @FXML
    protected TextField appointmentSearchField;

    /**
     * Calls setTableColumns().
     * @param url passed in from the FXMLLoader in Window.java.
     * @param resourceBundle passed in from the FXMLLoader in Window.java.
     * @see #setTableColumns()
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();
    }
    /**
     * Sets the getters for the table columns in the appointments table.
     */
    protected void setTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("ContactId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("FormattedStartDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("FormattedEndDate"));
    }

    /**
     * Used to set the customer for the Customer this page before opening the window. This must be called before opening the window.
     * Added lambda expression to avoid repeating an updateTable method call.
     * @param customer the customer to set.
     */
    protected void setCustomer(Customer customer) {
        this.customer = customer;
        associatedAppointments = customer.getAssociatedAppointments();
        associatedAppointments.addListener((ListChangeListener<? super Appointment>) change -> updateTable());
        customerLabel.setText("Customer: " + customer.getName());
        updateTable();
    }

    /**
     * Called when the search button is clicked. Sets the table values to the results found from searching the
     * associatedAppointments list.
     * @param actionEvent the event fired by the search button.
     */
    @FXML
    private void onSearchClick(ActionEvent actionEvent) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        String searchParam = appointmentSearchField.getText();
        if (searchParam.length() == 0) {
            updateTable();
        }
        else {
            try {
                Integer id = Integer.parseInt(searchParam);
                for (Appointment appointment : associatedAppointments) {
                    if (appointment.getAppointmentId() == id) {
                        returnList.add(appointment);
                    }
                }
                if (returnList.isEmpty()) {
                    openNotifyWindow(actionEvent, "notify.noAppointmentFound");
                } else appointmentsTable.setItems(returnList);
            } catch (NumberFormatException ignored) {
                openNotifyWindow(actionEvent, "notify.idOnly");
            }
        }
    }

    /**
     * Opens up a new window to add an associated appointment for the current customer.
     * @param actionEvent fired by the add button. Used to prevent further action on form before closing open window.
     */
    @FXML
    private void onAddClick(ActionEvent actionEvent) {
        Window addAppointment = new Window("appointment.fxml", "Add Appointment");
        AppointmentManagement controller = (AppointmentManagement) addAppointment.getController();
        controller.setCustomer(customer);
        addAppointment.showWindowAndWait(actionEvent);
    }

    /**
     * Opens up a new window to alter a selected customer's appointment. Opens an error window if no customer is selected.
     * @param actionEvent fired by the modify button. Used to prevent further action on form before closing open window.
     */
    @FXML
    private void onModifyClick(ActionEvent actionEvent) {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            Window modifyAppointment = new Window("appointment.fxml", "Modify Appointment");
            AppointmentManagement controller = (AppointmentManagement) modifyAppointment.getController();
            controller.setCustomer(customer);
            controller.setAppointment(appointment);
            modifyAppointment.showWindowAndWait(actionEvent);
            appointmentsTable.refresh();
        }
        else {
            openNotifyWindow(actionEvent, "notify.pleaseSelect", "word.an", "word.appointment");
        }
    }

    /**
     * Deletes the appointment selected in the appointments table. Opens an error window if no appointment selected.
     * @param actionEvent fired by the delete button. Used to open notification window.
     */
    @FXML
    private void onDeleteClick(ActionEvent actionEvent) {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            if (Instance.deleteAppointment(appointment)) {
                openNotifyWindow(actionEvent, "notify.appointmentWithId",
                        String.valueOf(appointment.getAppointmentId()),
                        "word.deletion",
                        "notify.success"
                );
                associatedAppointments.remove(appointment);
            }
            else {
                openNotifyWindow(actionEvent, "notify.appointmentWithId",
                        String.valueOf(appointment.getAppointmentId()),
                        "word.deletion",
                        "notify.fail"
                        );
            }
        }
        else {
            openNotifyWindow(actionEvent, "notify.selectAppointment");
        }
    }

    /**
     * Updates the appointment table based on the selected tab.
     */
    @FXML
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

    /**
     * Gets the appointments from the customers list of associated appointment and compare to the params to see if it's
     * between those dates.
     * @param after the start date to compare to. Usually today's date.
     * @param before the end date to compare to.
     * @return
     */
    protected ObservableList<Appointment> getAppointments(ZonedDateTime after, ZonedDateTime before) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();

        for (Appointment appointment : associatedAppointments){
            ZonedDateTime appointmentDate = appointment.getStartDate();
            if (appointmentDate.isAfter(after) && appointmentDate.isBefore(before)) {
                returnList.add(appointment);
            }
        }
        return returnList;
    }
}

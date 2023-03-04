package com.controllers;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.User;
import com.utils.Query;
import com.window.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.logging.*;

public class Login extends Controller implements Initializable {
    private User user;
    private String validUsername, validPassword, inputUsername, inputPassword;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label tzLabel;
    private FileHandler loginHandler;
    private final Logger loginLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);;

    /**
     * Sets the label value of the timezone and creates a login logger.
     * @param url passed from FXMLLoader.
     * @param resourceBundle passed from FXMLLoader.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tzLabel.setText(resourceBundle.getString("label.timezone") + Instance.SYSTEMZONEID);
        try {
            loginHandler = new FileHandler("./login_activity.txt", true);
            loginLogger.addHandler(loginHandler);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Method called when the login button is clicked. Validates credentials and opens main menu if creds are correct.
     * Logs successful login.
     * @param actionEvent fired by the button. Used to open the next window.
     */
    @FXML
    private void login(ActionEvent actionEvent) {
        if (validateCredentials(actionEvent)) {
            loginLogger.log(Level.INFO, "User with ID " + user.getId() + " successfully logged in.");
            Instance.setActiveUser(user);
            checkUpcomingAppointments(actionEvent);
            Window.changeScene(actionEvent, "main-menu.fxml", "Main Menu");
        }
        else openNotifyWindow(actionEvent, "error.incorrectCreds");
    }

    /**
     * Runs a query to get the login credentials for the specified user.
     * @param actionEvent passed in from login method.
     * @return true if credentials entered equal credentials queried.
     */
    @FXML
    private boolean validateCredentials(ActionEvent actionEvent) {
        inputUsername = userNameField.getText();
        inputPassword = passwordField.getText();
        queryUser();
        if (validateUsername() && validatePassword()) {
            actionEvent.consume();
            return true;
        }
        actionEvent.consume();
        return false;
    }

    /**
     * Checks the input username against the username provided by the query. Probably don't need this as the query will
     * fail if no matching username is found.
     * @return true if matches, false otherwise.
     */
    private boolean validateUsername() {
        if (user != null) {
            if (user.getUsername().equals(inputUsername)) return true;
        }
        return false;
    }

    /**
     * Compares the input password against the password returned by query. Logs whether the password was incorrect.
     * @return true if passwords match.
     */
    private boolean validatePassword() {
        if (user.getPassword().equals(inputPassword)) return true;
        else {
            loginLogger.log(Level.INFO, "User with ID " + user.getId() + " entered an incorrect password.");
            return false;
        }
    }

    /**
     * Queries the database for a user matching the input username and creates a User object.
     */
    private void queryUser() {
        ResultSet results = Query.selectConditional(
                "User_Id, User_Name, Password",
                "users",
                "User_Name = ",
                inputUsername
        );
        try {
            while (results.next()) {
                user = new User(
                        results.getInt(1),
                        results.getString(2),
                        results.getString(3)
                );
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks to see if there are any appointments within 15 minutes of a user logging in.
     * @param actionEvent used to open the notify window.
     */
    private void checkUpcomingAppointments(ActionEvent actionEvent) {
        boolean upcomingAppointments = false;
        ZonedDateTime now = ZonedDateTime.now(Instance.SYSTEMZONEID);
        for (Appointment appointment : Instance.getAllAppointments()) {
            ZonedDateTime start = appointment.getStartDate();
            if (start.isAfter(now) && (start.isBefore(now.plusMinutes(15)) || start.isEqual(now.plusMinutes(15)))) {
                String customer = Instance.lookupCustomer(appointment.getCustomerId()).getName();
                String id = String.valueOf(appointment.getAppointmentId());
                openNotifyWindow(
                        actionEvent,
                        "notify.appointmentWith1",
                         customer,
                        "notify.appointmentWith2",
                        id
                );
                upcomingAppointments = true;
            }
        }
        if (!upcomingAppointments) {
            openNotifyWindow(actionEvent, "notify.noAppointments");
        }
    }
}

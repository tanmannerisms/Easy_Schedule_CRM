package com.easyschedule;

import com.location.Division;
import com.people.*;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Instance {
    private static final String CUSTOMER_TABLE = "client_schedule.customers";
    private static User activeUser;
    public static ObservableList<Customer> allCustomers = Query.getAllCustomers();
    public static ObservableList<Contact> allContacts = Query.getAllContacts();
    public static ObservableList<Appointment> allAppointments = Query.getAllAppointments();
    public static ObservableList<Division> allDivisions;

    public static void updateCustomers() {
        allCustomers = Query.getAllCustomers();
    }
    public static void updateContacts() {
        allContacts = Query.getAllContacts();
    }
    public static void updateAppointments() {
        allAppointments = Query.getAllAppointments();
    }
    public static void updateDivisions() {
        allDivisions.clear();
        ResultSet results = Query.selectAll(
                "Division_ID, Country_ID, Division",
                "first_level_divisions"
                );
        try {
            while (results.next()) {
                Division newDivision = new Division(
                        results.getInt(1),
                        results.getInt(3),
                        results.getString(2)
                );
                allDivisions.add(newDivision);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public static Customer lookupCustomer(int customerId) {
        for (Customer returnCustomer : allCustomers) {
            if (returnCustomer.getId() == customerId) {
                return returnCustomer;
            }
        }
        return null;
    }
    public static ObservableList<Customer> lookupCustomer(String name) {
        ObservableList<Customer> returnList = FXCollections.observableArrayList();
        for (Customer customer : allCustomers) {
            if ((customer.getName().toLowerCase().contains(name.toLowerCase()))) {
                returnList.add(customer);
            }
        }
        return returnList;
    }
    public static boolean deleteCustomer(Customer customer) {
        String condition = "Customer_ID = " + customer.getId();
        if (!Query.delete(CUSTOMER_TABLE, condition)){
            return false;
        }
        allCustomers.remove(customer);
        return true;
    }
    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User user) {
        activeUser = user;
    }
    public static ObservableList<Appointment> getCustomerAppointments(Customer customer) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments) {
            if (appointment.getCustomerId() ==  customer.getId()) {
                returnList.add(appointment);
            }
        }
        return allAppointments;
    }
    public static Contact lookupContact(int contactId) {
        for (Contact contact : allContacts) {
            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }
}

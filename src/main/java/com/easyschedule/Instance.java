package com.easyschedule;

import com.people.*;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Instance {
    private static User activeUser;
    public static ObservableList<Customer> allCustomers;
    public static ObservableList<Contact> allContacts;
    public static ObservableList<Appointment> allAppointments;

    public static void updateData() {
        allCustomers = Query.getAllCustomers();
        allContacts = Query.getAllContacts();
        allAppointments = Query.getAllAppointments();
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
    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User user) {
        activeUser = user;
    }
    public static ObservableList<Appointment> getAppointments(Customer customer) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        for (Appointment appointment: allAppointments) {
            if (appointment.getCustomerId() ==  customer.getId()) {
                returnList.add(appointment);
            }
        }
        return allAppointments;
    }
}

package com.easyschedule;

import com.people.Contact;
import com.people.Customer;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Instance {
    private static User activeUser;
    public static ObservableList<Customer> allCustomers;
    public static ObservableList<Contact> allContacts;
    public static ObservableList<Appointment> allAppointments;

    public static void updateData() {
        allCustomers = FXCollections.observableArrayList(Query.getAllCustomers());
/*
        allContacts = FXCollections.observableArrayList(Query.getAllContacts);
        allAppointments = FXCollections.observableArrayList(Query.getAllAppointments);
*/
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

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        Instance.userId = userId;
    }
}

package com.easyschedule;

import com.people.Contact;
import com.people.Customer;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Instance {
    public static ObservableList<Customer> allCustomers;
    public static ObservableList<Contact> allContacts;
    public static ObservableList<Appointment> allAppointments;

    Instance() {
        allCustomers = FXCollections.observableArrayList(Query.getAllCustomers());
/*
        allContacts = FXCollections.observableArrayList(Query.getAllContacts);
        allAppointments = FXCollections.observableArrayList(Query.getAllAppointments);
*/
    }
}

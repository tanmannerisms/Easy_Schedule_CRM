package com.easyschedule;

import com.location.Country;
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
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    public static void updateAllLists() {
       updateCustomers();
       updateContacts();
       updateAppointments();
       updateDivisions();
       updateCountries();
    }

    public static void updateCustomers() {
        allCustomers.clear();
        ResultSet results = Query.selectAll(
                "Customer_Id, Customer_Name, Address, Postal_Code, Phone, Division_Id",
                "customers"
        );
        try {
            while (results.next()) {
                Customer newCustomer = new Customer(
                        results.getInt(1),
                        results.getString(2),
                        results.getString(3),
                        results.getString(4),
                        results.getString(5),
                        results.getInt(6)
                );
                allCustomers.add(newCustomer);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateContacts() {
        allContacts.clear();
        ResultSet results = Query.selectAll(
                "*",
                "contacts"
        );
        try {
            while (results.next()) {
                Contact newContact = new Contact(
                        results.getInt(1),
                        results.getString(2),
                        results.getString(3)
                );
                allContacts.add(newContact);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateAppointments() {
        allAppointments.clear();
        ResultSet results = Query.selectAll(
                "Appointment_Id, User_ID, Customer_ID, Contact_ID, Title, Description, Location, Type, Start, End",
                "appointments"
        );
        try {
            while (results.next()) {
                Appointment newAppointment = new Appointment(
                        results.getInt(1),
                        results.getInt(2),
                        results.getInt(3),
                        results.getInt(4),
                        results.getString(5),
                        results.getString(6),
                        results.getString(7),
                        results.getString(8),
                        results.getDate(9),
                        results.getDate(10)
                );
                allAppointments.add(newAppointment);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateDivisions() {
        allDivisions.clear();
        ResultSet results = Query.selectAllDivisions();/*Query.selectAll(
                "Division_ID, Country_ID, Division",
                "first_level_divisions"
                );*/
        try {
            while (results.next()) {
                Division newDivision = new Division(
                        results.getInt(1),
                        results.getInt(2),
                        results.getString(3)
                );
                allDivisions.add(newDivision);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateCountries() {
        allCountries.clear();
        ResultSet results = Query.selectAll(
                "*",
                "countries"
        );
        try {
            while (results.next()) {
                Country newCountry = new Country(
                        results.getInt(1),
                        results.getString(2)
                );
                allCountries.add(newCountry);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static ObservableList<Division> getAllDivisions() {
        return allDivisions;
    }

    public static ObservableList<Country> getAllCountries() {
        return allCountries;
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
        if (!Query.delete(CUSTOMER_TABLE, "Customer_ID = ", String.valueOf(customer.getId()))){
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
    public static Division getDivision(int divisionId) {
        for (Division division : allDivisions) {
            if (division.getDivisionId() == divisionId) {
                return division;
            }
        }
        return null;
    }
    public static ObservableList<Division> getDivision(Country country) {
        ObservableList<Division> returnList = FXCollections.observableArrayList();

        for (Division division : allDivisions) {
            if (division.getCountryId() == country.getCountryId()) {
                returnList.add(division);
            }
        }

        return returnList;
    }
    public static Country getCountry(int countryId) {
        System.out.println(allCountries.get(countryId - 1).getCountryName());

        return allCountries.get(countryId - 1);
    }
}

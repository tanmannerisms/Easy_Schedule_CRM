package com.easyschedule;

import com.location.Country;
import com.location.Division;
import com.people.Contact;
import com.people.Customer;
import com.people.User;
import com.utils.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Instance {
    public static final ZoneId SYSTEMZONEID = ZoneId.systemDefault();
    public static final ZoneId BUSINESSZONEID = ZoneId.of("America/New_York");
    private static final String CUSTOMER_TABLE = "client_schedule.customers";
    private static final Locale SYSTEMLOCALE = Locale.getDefault();
    public static final ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "com.easyschedule.lang.Nat",
            SYSTEMLOCALE
    );
    private static User activeUser;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    public static void updateAllLists() {
       updateCustomerList();
       updateContactList();
       updateAppointmentList();
       updateDivisionList();
       updateCountryList();
    }

    public static void updateCustomerList() {
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
    public static void updateContactList() {
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
    public static void updateAppointmentList() {
        allAppointments.clear();
        ResultSet results = Query.selectAll(
                "Appointment_Id, User_ID, Customer_ID, Contact_ID, Title, Description, Location, Type, Start, End",
                "appointments"
        );
        try {
            while (results.next()) {

                Timestamp start = results.getTimestamp(9);
                Timestamp end = results.getTimestamp(10);

                Appointment newAppointment = new Appointment(
                        results.getInt(1),
                        results.getInt(2),
                        results.getInt(3),
                        results.getInt(4),
                        results.getString(5),
                        results.getString(6),
                        results.getString(7),
                        results.getString(8),
                        convertToLocal(start),
                        convertToLocal(end)
                );
                allAppointments.add(newAppointment);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static ZonedDateTime convertToLocal(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        return ZonedDateTime.ofInstant(instant, SYSTEMZONEID);
    }
    public static void updateDivisionList() {
        allDivisions.clear();
        ResultSet results = Query.selectAll(
                "Division_ID, Country_ID, Division",
                "first_level_divisions"
                );
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
    public static void updateCountryList() {
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
    public static void addCustomer(Customer customer) {
        long now = getCurrentDateTime();
        Query.insert(
                "customers",
                "Customer_Name, Phone, Address, Postal_Code, Division_Id, Create_Date, Created_By",
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getPostalCode(),
                String.valueOf(customer.getDivisionId()),
                String.valueOf(now),
                getActiveUser().getName()
                );
        ResultSet resultSet = Query.selectConditional(
                "Customer_Id",
                "customers",
                "Customer_Name = ? AND Address = ",
                customer.getName(),
                customer.getAddress());
        try {
            while (resultSet.next()) {
                customer.setId(resultSet.getInt(1));
            }
        } catch (SQLException e){

        }
        allCustomers.add(customer);
    }
    public static void updateCustomer(Customer customer) {
        // Get current UTC time in epoch milliseconds
        long now = getCurrentDateTime();
        Query.update(
                "customers",
                "Customer_Id = " + customer.getId(),
                "Customer_Name, Phone, Address, Postal_Code, Division_Id, Last_Update, Last_Updated_By",
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getPostalCode(),
                String.valueOf(customer.getDivision().getId()),
                String.valueOf(now),
                Instance.getActiveUser().getName()
        );
    }
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }
    public static void addAppointment(Appointment appointment) {
        Long now = getCurrentDateTime();
        Query.insert(
                "appointments",
                "Title, Description, Location, Type, Start, End, Create_Date, Created_By, Customer_ID, User_Id, Contact_Id",
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.getLocation(),
                appointment.getType(),
                String.valueOf(appointment.getStartDate().toInstant().toEpochMilli()),
                String.valueOf(appointment.getEndDate().toInstant().toEpochMilli()),
                now.toString(),
                Instance.getActiveUser().getName(),
                String.valueOf(appointment.getCustomerId()),
                String.valueOf(appointment.getUserId()),
                String.valueOf(appointment.getContactId())
        );
        ResultSet resultSet = Query.selectConditional(
                "Appointment_Id",
                "appointments",
                "Start = ? AND End = ",
                String.valueOf(appointment.getStartDate().toInstant().toEpochMilli()),
                String.valueOf(appointment.getEndDate().toInstant().toEpochMilli())
        );
        try {
            while (resultSet.next()) {
                appointment.setAppointmentId(resultSet.getInt(1));
            }
        } catch (SQLException ignored){}
    }
    public static void updateAppointment(Appointment appointment) {
        Long now = getCurrentDateTime();
        Query.update(
                "appointments",
                "Appointment_Id = " + appointment.getAppointmentId(),
                "Title, Description, Location, Type, Start, End, Last_Update, Last_Updated_By, User_Id, Contact_Id",
                appointment.getTitle(),
                appointment.getDescription(),
                appointment.getLocation(),
                appointment.getType(),
                String.valueOf(appointment.getStartDate().toInstant().toEpochMilli()),
                String.valueOf(appointment.getEndDate().toInstant().toEpochMilli()),
                now.toString(),
                getActiveUser().getName(),
                String.valueOf(appointment.getUserId()),
                String.valueOf(appointment.getContactId())
                );
    }
    public static void deleteAppointment(Appointment appointment) {
        Query.delete(
                "appointments",
                "Appointment_Id = ",
                String.valueOf(appointment.getAppointmentId())
        );
        allAppointments.remove(appointment);
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
        return returnList;
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
            if (division.getId() == divisionId) {
                return division;
            }
        }
        return null;
    }
    public static ObservableList<Division> getDivision(Country country) {
        ObservableList<Division> returnList = FXCollections.observableArrayList();

        for (Division division : allDivisions) {
            if (division.getCountryId() == country.getId()) {
                returnList.add(division);
            }
        }

        return returnList;
    }
    public static int getDivision(String divisionName) {
        for (Division division : allDivisions) {
            if (division.getName() == divisionName) {
                return division.getId();
            }
        }
        return 0;
    }
    public static Country getCountry(int countryId) {
        for (Country country : allCountries) {
            if (country.getId() == countryId) {
                return country;
            }
        }
        return null;
    }
    public static Country getCountry(String countryName) {
        for (Country country : allCountries) {
            if (country.getName() == countryName) {
                return country;
            }
        }
        return null;
    }
    public static Contact getContact(int contactId) {
        for (Contact contact : allContacts) {
            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }
    public static Contact getContact(String contactName) {
        for (Contact contact : allContacts) {
            if (contact.getName() == contactName) {
                return contact;
            }
        }
        return null;
    }
    private static long getCurrentDateTime() {
        return Instant.now().toEpochMilli();
    }
}

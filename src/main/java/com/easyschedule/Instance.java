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

    /**
     * Used for setting the values for all the lists that will be used throughout the program.
     * @see #updateAppointmentList()
     * @see #updateCustomerList()
     * @see #updateContactList()
     * @see #updateDivisionList()
     * @see #updateCountryList()
     */
    public static void updateAllLists() {
        updateAppointmentList();
        updateCustomerList();
        updateContactList();
        updateDivisionList();
        updateCountryList();
    }

    /**
     * Queries the customer table to obtain a list of all customers. Sets Instance.allCustomers.
     * @see Query#selectAll(String, String)
     */
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

    /**
     * Queries the contact table to obtain a list of all the contacts. Sets Instance.allContacts.
     * @see Query#selectAll(String, String)
     */
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

    /**
     * Queries the appointments table to get a list of all the appointments. Sets Instance.allAppointments
     * @see Query#selectAll(String, String)
     */
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

    /**
     * Converts a MySQL Timestamp to a ZonedDateTime with the system Zone ID by converting the Timestamp to an Instant on
     * the java epoch timeline, then to a ZonedDateTime.
     * @param timestamp a timestamp retried by a MySQL query,
     * @return the ZonedDateTime of the timestamp param with the system's zone ID.
     */
    private static ZonedDateTime convertToLocal(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        return ZonedDateTime.ofInstant(instant, SYSTEMZONEID);
    }

    /**
     * Queries the divisions table to get a list of all the Divisions. Sets allDivisions list. This list is concrete unless
     * this method is run again.
     * @see Query#selectAll(String, String)
     */
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

    /**
     * Queries the country table to get a list of all the Countries. Sets allCountries list. This list is concrete unless
     * this method is run again.
     * @see Query#selectAll(String, String)
     */
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

    /**
     * Getter for allCustomers
     * @return allCustomers.
     */
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    /**
     * Tries to insert a new row into the customers table. If successful, the customer is added to allCustomers list.
     * @param customer the new customer to try adding to the database.
     * @see Query#insert(String, String, String...)
     */
    public static void addCustomer(Customer customer) {
        long now = getCurrentDateTime();
        if (Query.insert(
                "customers",
                "Customer_Name, Phone, Address, Postal_Code, Division_Id, Create_Date, Created_By",
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getPostalCode(),
                String.valueOf(customer.getDivisionId()),
                String.valueOf(now),
                getActiveUser().getName()
        ))
        {
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
            } catch (SQLException e){}
            allCustomers.add(customer);
        }
    }

    /**
     * Updates the customer row in customers where the ID is matching the customer ID. Object must be updated before calling!
     * @param customer the customer to be updated.
     */
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
                String.valueOf(customer.getDivisionId()),
                String.valueOf(now),
                Instance.getActiveUser().getName()
        );
    }

    /**
     * Gets allContacts
     * @return all contacts Observable List
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /**
     * Gets all appointments
     * @return all appointments Observable List
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /**
     * Tries to add an appointment using a newly created Appointment object. If successful, add the appointment to the allAppointments list.
     * @param appointment the newly created appointment to add to the database.
     * @see Query#insert(String, String, String...)
     */
    public static void addAppointment(Appointment appointment) {
        Long now = getCurrentDateTime();
        if (Query.insert(
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
        ))
        {
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
            allAppointments.add(appointment);
        }
    }

    /**
     * Tries to update the appointment in the database. Appointment object must be updated before calling this method.
     * @param appointment the appointment to be updated in the database.
     */
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

    /**
     * Tries to remove an appointment from the database. Removes the appointment from allAppointments list if successful.
     * @param appointment the appointment to be removed.
     * @return true if the deletion was successful, false if not.
     * @see Query#delete(String, String, String)
     */
    public static boolean deleteAppointment(Appointment appointment) {
        if (Query.delete(
                "appointments",
                "Appointment_Id = ",
                String.valueOf(appointment.getAppointmentId())
        )) {
            allAppointments.remove(appointment);
            return true;
        }
        else return false;
    }

    /**
     * Gets allDivisions list.
     * @return the list of all known divisions.
     */
    public static ObservableList<Division> getAllDivisions() {
        return allDivisions;
    }

    /**
     * Gets allCountries list.
     * @return the list of all known countries.
     */
    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    /**
     * Looks up a Customer object from the allCustomers list based on a customer ID.
     * @param customerId the customer to search for
     * @return customer if found, null if not.
     */
    public static Customer lookupCustomer(int customerId) {
        for (Customer returnCustomer : allCustomers) {
            if (returnCustomer.getId() == customerId) {
                return returnCustomer;
            }
        }
        return null;
    }

    /**
     * Looks up an Customer objects from the allCustomers list based on a customer name.
     * @param name the String to search for
     * @return an ObservableList of Customers with names that contain the parameter String.
     */
    public static ObservableList<Customer> lookupCustomer(String name) {
        ObservableList<Customer> returnList = FXCollections.observableArrayList();
        for (Customer customer : allCustomers) {
            if ((customer.getName().toLowerCase().contains(name.toLowerCase()))) {
                returnList.add(customer);
            }
        }
        return returnList;
    }

    /**
     * Tries to delete a Customer from the database matching the ID of the Customer object passed in. Removes the Customer
     * object from allCustomers if successful.
     * @param customer the Customer to delete.
     * @return true if success, false if SQL statement fails.
     */
    public static boolean deleteCustomer(Customer customer) {
        if (!Query.delete(CUSTOMER_TABLE, "Customer_ID = ", String.valueOf(customer.getId()))){
            return false;
        }
        allCustomers.remove(customer);
        return true;
    }

    /**
     * Gets the User object of the currently logged-in User.
     * @return the activeUser
     */
    public static User getActiveUser() {
        return activeUser;
    }

    /**
     * Sets the active user for the current instance. Only one known usage at time of writing.
     * @param user the user to set.
     * @see com.controllers.Login
     */
    public static void setActiveUser(User user) {
        activeUser = user;
    }

    /**
     * Gets a list of customer appointments from the Customer object passed in. Compares the customer ID with the appointment's
     * customer ID to determine if the appointment is referencing the customer.
     * @param customer the customer to get appointments for.
     * @return the list of appointments where the Customer object's ID is listed as the customer ID.
     */
    public static ObservableList<Appointment> getCustomerAppointments(Customer customer) {
        ObservableList<Appointment> returnList = FXCollections.observableArrayList();
        for (Appointment appointment : allAppointments) {
            if (appointment.getCustomerId() ==  customer.getId()) {
                returnList.add(appointment);
            }
        }
        return returnList;
    }

    /**
     * Finds a Contact object with the matching contact ID
     * @param contactId the ID of the contact to find.
     * @return the Contact object with ID matching the parameter.
     */
    public static Contact lookupContact(int contactId) {
        for (Contact contact : allContacts) {
            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Finds a Division object with the specified division ID.
     * @param divisionId the ID of the Division Object to search for.
     * @return
     */
    public static Division getDivision(int divisionId) {
        for (Division division : allDivisions) {
            if (division.getId() == divisionId) {
                return division;
            }
        }
        return null;
    }

    /**
     * Finds Division objects associated with the Country object parameter.
     * @param country the Country to find the divisions for.
     * @return a list of Divisions with a country ID matching the Country's ID.
     */
    public static ObservableList<Division> getDivision(Country country) {
        ObservableList<Division> returnList = FXCollections.observableArrayList();

        for (Division division : allDivisions) {
            if (division.getCountryId() == country.getId()) {
                returnList.add(division);
            }
        }

        return returnList;
    }

    /**
     * Gets a Division object with the specified name.
     * @param divisionName the name of the division to search for.
     * @return the first occurrence of the name in the allDivisions list.
     */
    public static int getDivision(String divisionName) {
        for (Division division : allDivisions) {
            if (division.getName() == divisionName) {
                return division.getId();
            }
        }
        return 0;
    }

    /**
     * Gets the country matching the ID specified
     * @param countryId the ID of the country to find.
     * @return the country matching the country ID passed in.
     */
    public static Country getCountry(int countryId) {
        for (Country country : allCountries) {
            if (country.getId() == countryId) {
                return country;
            }
        }
        return null;
    }

    /**
     * Gets the first occurrence of a country object with the name specified.
     * @param countryName the name of the country to search for.
     * @return the country object if found.
     */
    public static Country getCountry(String countryName) {
        for (Country country : allCountries) {
            if (country.getName() == countryName) {
                return country;
            }
        }
        return null;
    }

    /**
     * Gets a contact from allContacts matching the ID specified. 
     * @param contactId the ID of the contact to search for.
     * @return the contact object if found.
     */
    public static Contact getContact(int contactId) {
        for (Contact contact : allContacts) {
            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Gets the first occurrence of the contact object matching the name specified
     * @param contactName the name to look for.
     * @return the contact matching the name if found.
     */
    public static Contact getContact(String contactName) {
        for (Contact contact : allContacts) {
            if (contact.getName() == contactName) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Gets the current time in milliseconds from Java epoch. Used for create/update date timestamps. 
     * @return current time in milliseconds.
     * @see Query#update(String, String, String, String...) 
     * @see Query#insert(String, String, String...)
     */
    private static long getCurrentDateTime() {
        return Instant.now().toEpochMilli();
    }
}

package com.utils;

import com.easyschedule.Appointment;
import com.easyschedule.Instance;
import com.people.Contact;
import com.people.Customer;
import com.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;

    public static boolean delete(String table, String condition) {
        sql = "DELETE FROM " + table + " WHERE " + condition;
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.execute();
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static User getUser(String username) {
        User newUser = new User();
        sql = "SELECT User_Id, User_Name, Password FROM users WHERE User_Name = ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                newUser = new User(resultSet.getInt("User_Id"),
                                   resultSet.getString("User_Name"),
                                   resultSet.getString("Password"));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return newUser;
    }
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        sql = "SELECT Customer_Id, Customer_Name, Address, Postal_Code, Phone, Division_Id FROM customers";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("Customer_Id");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postCode = resultSet.getString("Postal_code");
                String phone = resultSet.getString("Phone");
                int divisionId = resultSet.getInt("Division_Id");
                Customer customer = new Customer(id, name, address, postCode, phone, divisionId);
                customers.add(customer);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }
    public static String getDivision(int divisionId) {
        sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, divisionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Division");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        sql = "SELECT Appointment_Id AS ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID FROM appointments";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start"),
                        resultSet.getDate("End")
                );
                appointments.add(appointment);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return appointments;
    }
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        sql = "SELECT * FROM contacts;";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Contact contact = new Contact(
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Email")
                );
                contacts.add(contact);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contacts;
    }
}

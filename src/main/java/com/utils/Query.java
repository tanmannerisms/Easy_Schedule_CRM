package com.utils;

import com.easyschedule.Appointment;
import com.people.Contact;
import com.people.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Query {
    private static PreparedStatement statement;
    private static String sql;

    public static String getPassword(String userName) {
        sql = "SELECT Password FROM users WHERE User_Name = ?";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Password");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static String getUsername(String userName) {
        try {
            sql = "SELECT User_Name FROM users WHERE User_Name = ?";
            statement = JDBC.connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("User_Name");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
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
        sql = "SELECT * FROM appointments";
        try {
            statement = JDBC.connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

//              Add constructor call here

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

            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contacts;
    }
}

package com.utils;

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
}

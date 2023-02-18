package com.utils;

import com.people.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerQuery {
    private static ResultSet resultSet;
    private static PreparedStatement statement;
    private static String sql;
    public static ObservableList<Customer> queryAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        sql = "SELECT Customer_Id AS Id, Customer_Name AS Name, Address, Postal_Code, Phone, Division " +
                "FROM customers, first_level_divisions " +
                "WHERE customers.Division_ID = first_level_divisions.Division_ID";
        statement = JDBC.connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        while(resultSet.next()) {
            int id = resultSet.getInt("Id");
            String name = resultSet.getString("Name");
            String addr = resultSet.getString("Address");
            String postCode = resultSet.getString("Postal_code");
            String phone = resultSet.getString("Phone");
            String division = resultSet.getString("Division");
            Customer customer = new Customer(id, name, addr, postCode, phone, division);
            customers.add(customer);
        }
        return customers;
    }

}
